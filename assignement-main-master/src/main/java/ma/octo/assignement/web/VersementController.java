package ma.octo.assignement.web;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import ma.octo.assignement.domain.Compte;
import ma.octo.assignement.domain.Versement;
import ma.octo.assignement.dto.VersementDto;
import ma.octo.assignement.exceptions.CompteNonExistantException;
import ma.octo.assignement.exceptions.TransactionException;
import ma.octo.assignement.repository.CompteRepository;
import ma.octo.assignement.repository.VersementRepository;
import ma.octo.assignement.service.AutiService;

@RestController
class VersementController {

    @Autowired
    VersementRepository versementRepository;

    @Autowired
    CompteRepository compteRepository;

    private AutiService autiService;

    public static final int MONTANT_MAXIMAL = 10000;

    @GetMapping("/versements")
    public List<Versement> getAll() {
        List<Versement> items = versementRepository.findAll();

        if (!items.isEmpty()) {
            return items;
        }
        return null;
    }

    @PostMapping("/versements")
    @ResponseStatus(HttpStatus.CREATED)
    public void createTransaction(@RequestBody VersementDto versementDto)
            throws CompteNonExistantException, TransactionException {
        Compte compte = compteRepository.findByNrCompte(versementDto.getCompteBenificiaire());
        if (compte == null) {
            System.out.println("Compte non existant");
            throw new CompteNonExistantException();
        }

        if (versementDto.getMontantVirement() == null) {
            System.out.println("Montant vide");
            throw new TransactionException("Montant vide");
        } else if (versementDto.getMontantVirement().intValue() == 0) {
            System.out.println("Montant vide");
            throw new TransactionException("Montant vide");
        } else if (versementDto.getMontantVirement().intValue() < 10) {
            System.out.println("Montant minimal de virement non atteint");
            throw new TransactionException("Montant minimal de virement non atteint");
        } else if (versementDto.getMontantVirement().intValue() > MONTANT_MAXIMAL) {
            System.out.println("Montant maximal de virement dépassé");
            throw new TransactionException("Montant maximal de virement dépassé");
        }

        if (versementDto.getMotifVersement().length() < 0) {
            System.out.println("Motif vide");
            throw new TransactionException("Motif vide");
        }

        compte.setSolde(compte.getSolde().add(versementDto.getMontantVirement()));
        compteRepository.save(compte);

        Versement versement = new Versement();
        versement.setCompteBeneficiaire(compte);
        versement.setDateExecution(versementDto.getDateExecution());
        versement.setMontantVirement(versementDto.getMontantVirement());
        versement.setNom_prenom_emetteur(
                compte.getUtilisateur().getFirstname() + " " + compte.getUtilisateur().getLastname());
        versementRepository.save(versement);
        autiService = new AutiService();
        autiService.auditVersement("Versement de " + versementDto.getMontantVirement().intValue() + "DH dans le compte "
                + versementDto.getCompteBenificiaire());

    }

}