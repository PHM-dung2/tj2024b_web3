package web.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;
import web.model.entity.ImgEntity;
import web.model.entity.ProductEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Data @NoArgsConstructor @AllArgsConstructor
public class ProductDto {

    // * 제품 등록할 때 필요한 필드
    private String pname;               // 제품명
    private String pcontent;            // 제품설명
    private int pprice;                 // 제품가격
    private List<MultipartFile> files = new ArrayList<>(); // 제품이미지들
    private long cno;                   // 제품카테고리 번호
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    // * toEntity : 제품 등록시 사용
    public ProductEntity toEntity(){
        return ProductEntity.builder()
                .pname( this.pname )
                .pcontent( this.pcontent )
                .pprice( this.pprice )
                // cno 대신 CategoryEntity 넘기
                // mno 대신 MemberEntity 넘기
                .build();
    }

    // * 제품 조회할 때 필요한 필드
    private long pno; // 제품번호
    private int pview;
    private String memail;
    private String cname;
    private List< String > images = new ArrayList<>(); // 이미지들

    // *toDto : 제품전체조회, 제품개별 조회 사용
    public static ProductDto toDto( ProductEntity productEntity ){
        return ProductDto.builder()
                .pname( productEntity.getPname() )
                .pcontent( productEntity.getPcontent() )
                .pprice( productEntity.getPprice() )
                .cno( productEntity.getCategoryEntity().getCno() )
                .createAt( productEntity.getCreateAt() )
                .updateAt( productEntity.getUpdateAt() )
                .pno( productEntity.getPno() )
                .pview( productEntity.getPview() )
                .memail( productEntity.getMemberEntity().getMemail() ) // 등록한 회원 번호 조회
                .cno( productEntity.getCategoryEntity().getCno() ) // 카테고리 번호 조회
                .cname( productEntity.getCategoryEntity().getCname() ) // 카테고리 이름 조회
                .images( // 제품의 이미지들을 조회
                        productEntity.getImgEntityList().stream()
                                .map( ImgEntity::getIname )
                                .collect( Collectors.toList() )
                )
                .build();
    }

}
