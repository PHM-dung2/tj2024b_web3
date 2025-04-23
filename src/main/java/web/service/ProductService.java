package web.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
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
import java.util.Scanner;
import java.util.stream.Collectors;

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

    // 1.
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

    // 2. (카테고리별) 제품 전체조회 : 설계 : ?cno=1
    public List<ProductDto> allProducts( long cno ){
        // 1. 조회된 겨로가를 저장하는 리스트 변수
        List<ProductEntity> productEntityList = productRepository.findAll();
        // 2. cno에 따라 카테고리별 조회 vs 전체조회
        if( cno > 0 ){
            productEntityList = productRepository.findByCategoryEntity();
        }else{
            productEntityList = productRepository.findAll();
        }
        // 3. 조회한 결과 entity를 dto로 변환
        return productEntityList.stream()
                .map( ProductDto::toDto )
                .collect( Collectors.toList() );

    } // f end

    // 3. 제품 개별조회 : 설계 : ?pno=1
    public ProductDto viewProduct( int pno ){
        return null;
    } // f end

}
