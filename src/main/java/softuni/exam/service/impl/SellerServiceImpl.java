package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dtos.xmls.SellerImportDto;
import softuni.exam.models.dtos.xmls.SellerRootImportDto;
import softuni.exam.models.entities.Rating;
import softuni.exam.models.entities.Seller;
import softuni.exam.repository.SellerRepository;
import softuni.exam.service.SellerService;
import softuni.exam.util.Validations.ValidationUtil;
import softuni.exam.util.XmlParserUtil.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Service
public class SellerServiceImpl implements SellerService {
    private static final String SELLER_PATH = "src/main/resources/files/xml/sellers.xml";

    private final SellerRepository sellerRepository;
    private final XmlParser xmlParser;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;

    public SellerServiceImpl
            (SellerRepository sellerRepository, XmlParser xmlParser, ValidationUtil validationUtil, ModelMapper modelMapper) {

        this.sellerRepository = sellerRepository;
        this.xmlParser = xmlParser;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
    }

    @Override
    public boolean areImported() {
        return this.sellerRepository.count() > 0;
    }

    @Override
    public String readSellersFromFile() throws IOException {
        return String.join("", Files.readAllLines(Path.of(SELLER_PATH)));
    }

    @Override
    public String importSellers() throws IOException, JAXBException {
        StringBuilder sb = new StringBuilder();
        SellerRootImportDto sellerRootImportDto = this.xmlParser.parseXml(SellerRootImportDto.class, SELLER_PATH);

        for (SellerImportDto sellerImportDto : sellerRootImportDto.getSellerImportDtos()) {
            Rating rating;
            try {
                rating = Rating.valueOf(sellerImportDto.getRating());
            } catch (Exception e) {
                sb.append("Invalid seller").append(System.lineSeparator());
                continue;
            }

            Optional<Seller> byEmail = this.sellerRepository.findByEmail(sellerImportDto.getEmail());
            Rating.valueOf(sellerImportDto.getRating());//->very_good - > Exception
            if (this.validationUtil.isValid(sellerImportDto) && byEmail.isEmpty()) {
                Seller seller = this.modelMapper.map(sellerImportDto, Seller.class);
                seller.setRating(rating);
                this.sellerRepository.saveAndFlush(seller);
                sb.append(String.format("Successfully added seller %s - %s",
                        seller.getLastName(),seller.getEmail())).append(System.lineSeparator());

            } else {
                sb.append("Invalid seller").append(System.lineSeparator());
            }
        }

        return sb.toString();
    }
}
