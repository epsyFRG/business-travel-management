package com.emiliano.business_travel_management.services;

import com.emiliano.business_travel_management.entities.Dipendente;
import com.emiliano.business_travel_management.exceptions.BadRequestException;
import com.emiliano.business_travel_management.exceptions.NotFoundException;
import com.emiliano.business_travel_management.payload.NewDipendenteDTO;
import com.emiliano.business_travel_management.payload.UpdateDipendenteDTO;
import com.emiliano.business_travel_management.repositories.DipendentiRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;
import com.cloudinary.Cloudinary;
import org.springframework.web.multipart.MultipartFile;
import java.util.Map;

@Service
@Slf4j
public class DipendentiService {

    @Autowired
    private DipendentiRepository dipendentiRepository;
    @Autowired
    private Cloudinary imageUploader;

    public Page<Dipendente> findAll(int pageNumber, int pageSize, String sortBy) {
        if (pageSize > 50) pageSize = 50;
        Pageable pageable = PageRequest.of(pageNumber, pageSize, Sort.by(sortBy).ascending());
        return this.dipendentiRepository.findAll(pageable);
    }

    public Dipendente save(NewDipendenteDTO payload) {
        this.dipendentiRepository.findByEmail(payload.email()).ifPresent(dipendente -> {
            throw new BadRequestException("l'email " + dipendente.getEmail() + " è già in uso");
        });

        this.dipendentiRepository.findByUsername(payload.username()).ifPresent(dipendente -> {
            throw new BadRequestException("lo username " + dipendente.getUsername() + " è già in uso");
        });

        Dipendente newDipendente = new Dipendente(payload.username(), payload.nome(), payload.cognome(), payload.email());
        newDipendente.setImmagineProfilo("https://ui-avatars.com/api/?name=" + payload.nome() + "+" + payload.cognome());

        Dipendente savedDipendente = this.dipendentiRepository.save(newDipendente);

        log.info("dipendente salvato con id: " + savedDipendente.getId());

        return savedDipendente;
    }

    public Dipendente findById(UUID dipendenteId) {
        return this.dipendentiRepository.findById(dipendenteId).orElseThrow(() -> new NotFoundException(dipendenteId));
    }

    public Dipendente findByIdAndUpdate(UUID dipendenteId, UpdateDipendenteDTO payload) {
        Dipendente found = this.findById(dipendenteId);

        if (!found.getEmail().equals(payload.email())) {
            this.dipendentiRepository.findByEmail(payload.email()).ifPresent(dipendente -> {
                throw new BadRequestException("l'email " + dipendente.getEmail() + " è già in uso");
            });
        }

        if (!found.getUsername().equals(payload.username())) {
            this.dipendentiRepository.findByUsername(payload.username()).ifPresent(dipendente -> {
                throw new BadRequestException("lo username " + dipendente.getUsername() + " è già in uso");
            });
        }

        found.setUsername(payload.username());
        found.setNome(payload.nome());
        found.setCognome(payload.cognome());
        found.setEmail(payload.email());
        found.setImmagineProfilo("https://ui-avatars.com/api/?name=" + payload.nome() + "+" + payload.cognome());

        Dipendente modifiedDipendente = this.dipendentiRepository.save(found);

        log.info("dipendente modificato con id " + modifiedDipendente.getId());

        return modifiedDipendente;
    }

    public void findByIdAndDelete(UUID dipendenteId) {
        Dipendente found = this.findById(dipendenteId);
        this.dipendentiRepository.delete(found);
        log.info("dipendente eliminato con id " + dipendenteId);
    }

    public String uploadImmagineProfilo(UUID dipendenteId, MultipartFile file) {
        Dipendente dipendente = this.findById(dipendenteId);

        if (file.isEmpty()) throw new BadRequestException("file vuoto");
        if (file.getSize() > 5242880) throw new BadRequestException("file troppo grande");
        if (!file.getContentType().equals("image/png") && !file.getContentType().equals("image/jpeg")) {
            throw new BadRequestException("i formati permessi sono png e jpeg");
        }

        try {
            String imageURL = (String) imageUploader.uploader().upload(file.getBytes(), Map.of()).get("url");
            dipendente.setImmagineProfilo(imageURL);
            this.dipendentiRepository.save(dipendente);
            log.info("immagine profilo aggiornata per dipendente con id " + dipendenteId);
            return imageURL;
        } catch (Exception e) {
            throw new RuntimeException("errore durante upload immagine");
        }
    }
}


