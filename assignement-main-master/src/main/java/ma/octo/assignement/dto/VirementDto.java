package ma.octo.assignement.dto;

import java.math.BigDecimal;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VirementDto {
  private String nrCompteEmetteur;
  private String nrCompteBeneficiaire;
  private String motif;
  private BigDecimal montantVirement;
  private Date date;

}
