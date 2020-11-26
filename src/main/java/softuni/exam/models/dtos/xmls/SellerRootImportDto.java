package softuni.exam.models.dtos.xmls;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

@XmlRootElement(name = "sellers")
@XmlAccessorType(XmlAccessType.FIELD)
public class SellerRootImportDto {
    public List<SellerImportDto> getSellerImportDtos() {
        return sellerImportDtos;
    }

    public void setSellerImportDtos(List<SellerImportDto> sellerImportDtos) {
        this.sellerImportDtos = sellerImportDtos;
    }

    public SellerRootImportDto(List<SellerImportDto> sellers) {
        this.sellerImportDtos = sellers;
    }

    public SellerRootImportDto() {
    }

    @XmlElement(name = "seller")
    private  List<SellerImportDto> sellerImportDtos;


}
