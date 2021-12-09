package ma.octo.assignement.dto;

import java.math.BigDecimal;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VersementDto {
    private Long id;
    private BigDecimal montantVirement;
    private String compteBenificiaire;
    private String motifVersement;
    private Date dateExecution;

}
