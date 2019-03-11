package it.myti.academy.backend.controller;

import it.myti.academy.backend.model.Collo;
import it.myti.academy.backend.model.EventoParticle;
import it.myti.academy.backend.model.UnitaLogistica;
import it.myti.academy.backend.model.resp.UnitaLogisticheDettaglio;
import it.myti.academy.backend.repository.EventiParticleRepository;
import it.myti.academy.backend.service.ColloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by david at 2019-03-11
 */
@RestController
public class UnitaLogisticheController {

    @Autowired
    public ColloService colloService;

    @Autowired
    public EventiParticleRepository eventiParticleRepository;

    @GetMapping("/unitalogistiche/utente/{id}")
    public List<UnitaLogisticheDettaglio> getDettagliByUtente(@PathVariable("id") long id) {

        final List<Collo> spedizioniAttiveByUtente = colloService.getSpedizioniAttiveByUtente(id);

        final List<UnitaLogisticheDettaglio> collect = spedizioniAttiveByUtente.stream()
                .map(c -> {
                    UnitaLogisticheDettaglio dettaglio = new UnitaLogisticheDettaglio();
                    final UnitaLogistica unitaLogistica = c.getUnitaLogistica();
                    dettaglio.setId(unitaLogistica.getId());
                    dettaglio.setNome(unitaLogistica.getCodice());
                    dettaglio.setStatus(unitaLogistica.getStato());
                    final EventoParticle lastEvent = eventiParticleRepository.findFirstByOrderByRicevutoIlDesc();
                    dettaglio.fillByEventoParticle(lastEvent);
                    dettaglio.setSpedizione(c.getSpedizione());
                    dettaglio.setContenuto(c.getContenuti());
                    return dettaglio;
                })
                .collect(Collectors.toList());

        return collect;

    }

}
