package web.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import web.model.dto.CategoryDto;
import web.model.dto.ProductDto;
import web.model.entity.CategoryEntity;
import web.model.entity.ImgEntity;
import web.model.entity.MemberEntity;
import web.model.entity.ProductEntity;
import web.model.repository.CategoryRepository;
import web.model.repository.ImgRepository;
import web.model.repository.MemberRepository;
import web.model.repository.ProductRepository;
import web.util.FileUtil;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {
    // *
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;
    private final CategoryRepository categoryRepository;
    private final ImgRepository imgRepository;
    private final FileUtil fileUtil;

    // 1. 제품등록
    public boolean registerProduct( ProductDto productDto, int loginMno ){

        // 1. 현재 회원번호의 엔티티 찾기( 연관관계 ) FK, Optional : null 값 제어 기능 제공
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findById( loginMno );
        if( optionalMemberEntity.isEmpty() ){ return false; } // 만약에 조회된 회원엔티티가 없으면 false
        // 2. 현재 카테고리 번호의 엔티티 찾기( 연관관계 ) FK
        Optional<CategoryEntity> optionalCategoryEntity = categoryRepository.findById( productDto.getCno() );
        if( optionalCategoryEntity.isEmpty() ){ return false; }
        // 3. ProductDto를 ProductEntity 변환
        ProductEntity productEntity = productDto.toEntity();
        // 4. 단방향 관계(FK) 주입, cno[x] --> CategoryEntity
        productEntity.setCategoryEntity( optionalCategoryEntity.get() );
        productEntity.setMemberEntity( optionalMemberEntity.get() );
        // 5. 영속성 연결
        ProductEntity saveEntity = productRepository.save( productEntity );
        if( saveEntity.getPno() <= 0 ){ return false; } // 제품번호 0이하이면 실패
        // 6. 파일처리
        if( productDto.getFiles() != null && !productDto.getFiles().isEmpty() ){
            // 6-1 : 여러개 첨부파일이므로 반복문 확용
            for( MultipartFile file : productDto.getFiles() ){
                // 6-2 : FileUtil에서 업로드 메소드 호출(
                String saveFileName = fileUtil.fileUpload( file );
                // 6-3 : * 만약에 업로드 실패하면 트랜잭션 롤백 *
                if( saveFileName == null ){
                    // 6-4 : 강제 예외 발생
                    throw new RuntimeException("업로드 중에 오류 발생");
                }
                // 6-4 : 업로드 성공했으면 ImgEntity 만들기
                ImgEntity imgEntity = ImgEntity.builder().iname( saveFileName ).build();
                // 6-5 : * 단방향 관계(FK) 주입, pno[x] --> productEntity *
                imgEntity.setProductEntity( saveEntity );
                // 6-6 : ImgEntity 영속화
                imgRepository.save( imgEntity );
            }
        }
        return true;
    } // f end

//    // 2. (카테고리별) 제품 전체조회 : 설계 : ?cno=1
//    public List<ProductDto> allProducts( Long cno ){
//        // 1. 조회된 겨로가를 저장하는 리스트 변수
//        List<ProductEntity> productEntityList = productRepository.findAll();
//        // 2. cno에 따라 카테고리별 조회 vs 전체조회
//        System.out.println(cno);
//        if( cno != null && cno > 0 ){
//            productEntityList = productRepository.findByCategoryEntityCno( cno );
//        }else{
//            productEntityList = productRepository.findAll();
//        }
//        // 3. 조회한 결과 entity를 dto로 변환
//        return productEntityList.stream()
//                .map( ProductDto::toDto )
//                .collect( Collectors.toList() );
//
//    } // f end

    // 3. 제품 개별조회 : 설계 : ?pno=1
    public ProductDto viewProduct( Long pno ){
        // 1. pno에 해당하는 엔티티 조회
        Optional< ProductEntity > productEntityOptional = productRepository.findById( pno );
        // 2. 조회 결과 없으면 null
        if( productEntityOptional.isEmpty() ){ return null; }
        // 3. 조회 결과 있으면 엔티티 꺼내기     .get()
        ProductEntity productEntity = productEntityOptional.get();
        // 4. 조회수 증가, 기존 조회수 호출해서 +1 결과를 저장
        productEntity.setPview( productEntity.getPview() + 1 );
        // 5. 조호된 엔티티를 DTO로 변환
        return ProductDto.toDto( productEntity );
    } // f end

    // 4. 제품 개별삭제, + 이미지 삭제
    public boolean deleteProduct( Long pno, int loginMno ){
        // 1. pno에 해당하는 엔티티 찾기
        Optional<ProductEntity> productEntityOptional =
                productRepository.findById( pno );
        // 2. 없으면 null
        if( productEntityOptional.isEmpty() ){ return false; }
        // 3. 요청한 사람이 등록ㅎ나 제품인지 확인
        ProductEntity productEntity = productEntityOptional.get();
        if( productEntity.getMemberEntity().getMno() != loginMno ){
            // 만약에 제품 등록한 회원의 번호와 현재 로그인된 회원번호가 일치하지 않으면 false
            return false;
        }
        // 4. 서버에 저장된 (업로드) 이미지들 삭제
        List<ImgEntity> imgEntityList = productEntity.getImgEntityList();
        for( ImgEntity imgEntity : imgEntityList ){
            boolean result = fileUtil.fileDelete( imgEntity.getIname() ); // web2 작성한 파일삭제 메소드 참고
            if( result == false ){
                throw new RuntimeException("파일삭제 실패"); // 트랜잭션 롤백
            }
        }
        // 5. 이미지 모두 삭제했으면 제품 DB 삭제, ?? 이미지 db는 삭제 코드는 별도로 없다.
        // cascade = CascadeType.ALL 관계로 제품이 삭제되면 이미지 레코드도 같이 삭제한다.
        productRepository.deleteById( pno );
        return true;
    } // f end

    // 5. 제품 수정( + 이미지 추가 )
    public boolean updateProduct( ProductDto productDto, int loginMno ){
        // 1. 기존 제품의 정보(엔티티) 가져오기 // 없으면 취소 // Optional 클래스는 null 제어 메소드 제공
        Optional< ProductEntity > productEntityOptional = productRepository.findById( productDto.getPno() );
        if( productEntityOptional.isEmpty() ){ return false; } // 조회 엔티티가 없으면
        ProductEntity productEntity = productEntityOptional.get();
        // 2. 현재 토큰(로그인) 사람의 등록한 제품인지 인가확인 // 아니면 취소
        if( productEntity.getMemberEntity().getMno() != loginMno ){ return false; }
        // 3. 현재 수정할 카테고리 엔티티 가져오기 // 없으면 취소
        Optional< CategoryEntity > categoryEntityOptional = categoryRepository.findById( productDto.getCno() );
        if( categoryEntityOptional.isEmpty() ){ return false; }
        CategoryEntity categoryEntity = categoryEntityOptional.get();
        // 4. 제품 정보를 수정한다. // 오류 발생시 롤백한다.
            // - 조회한 기존의 제품 정보(엔티티)에서 set 이용한 수정
        productEntity.setPname( productDto.getPname() );
        productEntity.setPcontent( productDto.getPcontent() );
        productEntity.setPprice( productDto.getPprice() );
        productEntity.setCategoryEntity( categoryEntity ); // 찾은 엔티티
        // 5. 새로운 이미지가 있으면 FileUtil에서 업로드 함수 이용하여 업로드한다. // 오류 발생시 롤백한다.
        List< MultipartFile > newFile = productDto.getFiles();
        if( newFile != null && !newFile.isEmpty() ){ // 새로운 이미지가 존재하면
            for( MultipartFile file : newFile ){
                String saveFileName = fileUtil.fileUpload( file );
                if( saveFileName == null ){ throw new RuntimeException("파일 업로드 오류발생"); } // throw new 예외클래스명(); // 강제 예외 발생했다.
                // 새 이미지 처리
                ImgEntity imgEntity = ImgEntity.builder() // 이미지 엔티티 생성, 자바객체 <--영속-->
                        .iname( saveFileName )
                        .productEntity( productEntity )
                        .build();
                imgRepository.save( imgEntity ); // 이미지 렌더링 저장(영속) 자바객체 <--영속--> DB테이블레코드
            }
        }
        return true; // 6. 끝

    } // f ned

    // 6. 이미지 개별 삭제
    public Boolean deleteImage( long ino, int loginMno ){
        // 1. 이미지 엔티티 조회
        Optional< ImgEntity > imgEntityOptional = imgRepository.findById( ino );
        if( imgEntityOptional.isEmpty() ){ return false; }
        ImgEntity imgEntity = imgEntityOptional.get();
        // 2. 인가 확인
        if( imgEntity.getProductEntity().getMemberEntity().getMno() != loginMno ){ return false; }
        // 3. 물리적인 로컬 삭제
        String deleteFileName = imgEntity.getIname();
        boolean result = fileUtil.fileDelete( deleteFileName );
        if( result == false ){ throw new RuntimeException("파일 삭제 실패"); }
        // 4. 엔티티 삭제
        imgRepository.deleteById( ino );
        return true;
    } // f end

    // 7. 카테고리 조회
    public List<CategoryDto> allCategory(){
        // 1. 모든 카테고리 조회
        List< CategoryEntity > categoryEntityList = categoryRepository.findAll();
        // 2. List<Entity> --> List<Dto> 변환
        List< CategoryDto > categoryDtoList = categoryEntityList.stream()
                .map( CategoryDto::toDto )
                .collect( Collectors.toList() );
        // 3. 끝
        return categoryDtoList;
    } // f end

    // 2. 조회 : 검색 + 페이징처리
    public List< ProductDto > allProducts( Long cno, int page, int size, String keyword ){
        // 1. 페이징처리 설정, page-1 : 1페이지를 0으로 사용하므로  01, "
        Pageable pageable = PageRequest.of( page-1, size, Sort.by( Sort.Direction.DESC, "pno" ) );
        // Pageable : 인터페이스, import org.springframework.data.domain.Pageable;
        // PageReauest : 클래스(구현체)
            // .of( 페이지번호[0부터], 페이지별자료수, 정렬 )

        // 엔티티 조회
            // 예시) 전체조회 : productRepository.findALl( pageable )
            // 예시) 카테고리별조회 : produceRepository.만든함수명( pageable )
        Page<ProductEntity> productEntities = productRepository.findBySearch( cno, keyword, pageable );
        // 3. 반환타입
        List<ProductDto> productDtoList = productEntities.stream().map( ProductDto::toDto ).collect( Collectors.toList() );
        return productDtoList; // 4. 끝
    } // f ned

}
