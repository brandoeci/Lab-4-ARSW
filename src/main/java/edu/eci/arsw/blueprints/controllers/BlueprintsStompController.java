package edu.eci.arsw.blueprints.controllers;

import edu.eci.arsw.blueprints.dto.DrawEvent;
import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.persistence.BlueprintNotFoundException;
import edu.eci.arsw.blueprints.services.BlueprintsServices;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class BlueprintsStompController {

    private final BlueprintsServices services;
    private final SimpMessagingTemplate template;

    public BlueprintsStompController(BlueprintsServices services, SimpMessagingTemplate template) {
        this.services = services;
        this.template = template;
    }

    @MessageMapping("/draw")
    public void onDraw(DrawEvent evt) {
        try {
            services.addPoint(evt.author(), evt.name(), evt.point().x(), evt.point().y());
            Blueprint updated = services.getBlueprint(evt.author(), evt.name());

            template.convertAndSend(
                    "/topic/blueprints." + evt.author() + "." + evt.name(),
                    updated
            );
        } catch (BlueprintNotFoundException e) {
            // El plano no existe aún — el front debe crearlo primero vía CRUD antes de dibujar
        }
    }
}