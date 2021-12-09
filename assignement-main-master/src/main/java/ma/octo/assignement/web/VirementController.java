package ma.octo.assignement.web;

import ma.octo.assignement.domain.Compte;
import ma.octo.assignement.domain.Utilisateur;
import ma.octo.assignement.domain.Virement;
import ma.octo.assignement.dto.VirementDto;
import ma.octo.assignement.exceptions.CompteNonExistantException;
import ma.octo.assignement.exceptions.SoldeDisponibleInsuffisantException;
import ma.octo.assignement.exceptions.TransactionException;
import ma.octo.assignement.repository.CompteRepository;
import ma.octo.assignement.repository.UtilisateurRepository;
import ma.octo.assignement.repository.VirementRepository;
import ma.octo.assignement.service.AutiService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
class VirementController {

    public static final int MONTANT_MAXIMAL = 10000;

    Logger LOGGER = LoggerFactory.getLogger(VirementController.class);

    @Autowired
    private CompteRepository compteRepository;
    @Autowired
    private VirementRepository virementRepository;
    private AutiService autiService;
    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @GetMapping("/virements")
    List<Virement> loadAll() {
        List<Virement> all = virementRepository.findAll();

        if (CollectionUtils.isEmpty(all)) {
            return null;
        } else {
            return all;
        }
    }

    @GetMapping("/comptes")
    List<Compte> loadAllCompte() {
        List<Compte> all = compteRepository.findAll();

        if (CollectionUtils.isEmpty(all)) {
            return null;
        } else {
            return all;
        }
    }

    @GetMapping("/utilisateurs")
    List<Utilisateur> loadAllUtilisateur() {
        List<Utilisateur> all = utilisateurRepository.findAll();

        if (CollectionUtils.isEmpty(all)) {
            return null;
        } else {
            return all;
        }
    }

    @PostMapping("/executerVirements")
    @ResponseStatus(HttpStatus.CREATED)
    public void createTransaction(@RequestBody VirementDto virementDto)
            throws SoldeDisponibleInsuffisantException, CompteNonExistantException, TransactionException {
        Compte c1 = compteRepository.findByNrCompte(virementDto.getNrCompteEmetteur());
        Compte f12 = compteRepository
                .findByNrCompte(virementDto.getNrCompteBeneficiaire());

        if (c1 == null) {
            System.out.println("Compte Non existant");
            throw new CompteNonExistantException("Compte Non existant");
        }

        if (f12 == null) {
            System.out.println("Compte Non existant");
            throw new CompteNonExistantException("Compte Non existant");
        }

        if (virementDto.getMontantVirement() == null) { // removed the .equals() because it will throw a
                                                        // NullPointerException if the object is null
            System.out.println("Montant vide");
            throw new TransactionException("Montant vide");
        } else if (virementDto.getMontantVirement().intValue() == 0) {
            System.out.println("Montant vide");
            throw new TransactionException("Montant vide");
        } else if (virementDto.getMontantVirement().intValue() < 10) {
            System.out.println("Montant minimal de virement non atteint");
            throw new TransactionException("Montant minimal de virement non atteint");
        } else if (virementDto.getMontantVirement().intValue() > MONTANT_MAXIMAL) {
            System.out.println("Montant maximal de virement dépassé");
            throw new TransactionException("Montant maximal de virement dépassé");
        }

        if (virementDto.getMotif().length() < 0) {
            System.out.println("Motif vide");
            throw new TransactionException("Motif vide");
        }

        if (c1.getSolde().intValue() - virementDto.getMontantVirement().intValue() < 0) {
            LOGGER.error("Solde insuffisant pour l'utilisateur");
        }

        if (c1.getSolde().intValue() - virementDto.getMontantVirement().intValue() < 0) {
            LOGGER.error("Solde insuffisant pour l'utilisateur");
        }

        c1.setSolde(c1.getSolde().subtract(virementDto.getMontantVirement()));
        compteRepository.save(c1);

        f12
                .setSolde(new BigDecimal(f12.getSolde().intValue() + virementDto.getMontantVirement().intValue()));
        compteRepository.save(f12);

        Virement virement = new Virement();
        virement.setDateExecution(virementDto.getDate());
        virement.setCompteBeneficiaire(f12);
        virement.setCompteEmetteur(c1);
        virement.setMontantVirement(virementDto.getMontantVirement());

        virementRepository.save(virement);

        autiService = new AutiService();
        autiService.auditVirement("Virement depuis " + virementDto.getNrCompteEmetteur() + " vers " + virementDto
                .getNrCompteBeneficiaire() + " d'un montant de "
                + virementDto.getMontantVirement()
                        .toString());
    }

    private void save(Virement Virement) {
        virementRepository.save(Virement);
    }
}
