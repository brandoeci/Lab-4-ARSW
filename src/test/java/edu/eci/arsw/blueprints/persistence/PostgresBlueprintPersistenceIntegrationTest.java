package edu.eci.arsw.blueprints.persistence;

import edu.eci.arsw.blueprints.model.Blueprint;
import edu.eci.arsw.blueprints.model.Point;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Testcontainers
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("postgres")
class PostgresBlueprintPersistenceIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16");

    @Autowired
    private BlueprintPersistence persistence;

    @Test
    void shouldReturnSeedBlueprints() {
        Set<Blueprint> all = persistence.getAllBlueprints();
        assertThat(all).hasSizeGreaterThanOrEqualTo(3);
        assertThat(all).extracting(Blueprint::getAuthor).contains("john", "jane");
    }

    @Test
    void shouldReturnBlueprintWithPointsInOrder() throws BlueprintNotFoundException {
        Blueprint house = persistence.getBlueprint("john", "house");
        assertThat(house.getPoints())
                .containsExactly(new Point(0, 0), new Point(10, 0), new Point(10, 10), new Point(0, 10));
    }

    @Test
    void shouldThrowWhenBlueprintNotFound() {
        assertThrows(BlueprintNotFoundException.class,
                () -> persistence.getBlueprint("nobody", "nothing"));
    }

    @Test
    void shouldThrowWhenAuthorHasNoBlueprints() {
        assertThrows(BlueprintNotFoundException.class,
                () -> persistence.getBlueprintsByAuthor("nobody"));
    }

    @Test
    void shouldSaveAndRetrieveNewBlueprint() throws Exception {
        String name = "office-" + UUID.randomUUID();
        Blueprint bp = new Blueprint("maria", name, List.of(new Point(1, 1), new Point(2, 2)));

        persistence.saveBlueprint(bp);
        Blueprint stored = persistence.getBlueprint("maria", name);

        assertThat(stored.getPoints()).containsExactly(new Point(1, 1), new Point(2, 2));
    }

    @Test
    void shouldRejectDuplicateBlueprint() throws Exception {
        String name = "dup-" + UUID.randomUUID();
        Blueprint bp = new Blueprint("carlos", name, List.of(new Point(0, 0)));
        persistence.saveBlueprint(bp);

        assertThrows(BlueprintPersistenceException.class,
                () -> persistence.saveBlueprint(bp));
    }

    @Test
    void shouldAppendPointPreservingOrder() throws Exception {
        String name = "addpoint-" + UUID.randomUUID();
        Blueprint bp = new Blueprint("pedro", name, List.of(new Point(1, 1), new Point(2, 2)));
        persistence.saveBlueprint(bp);

        persistence.addPoint("pedro", name, 9, 9);

        Blueprint stored = persistence.getBlueprint("pedro", name);
        assertThat(stored.getPoints())
                .containsExactly(new Point(1, 1), new Point(2, 2), new Point(9, 9));
    }

    @Test
    void shouldThrowWhenAddingPointToMissingBlueprint() {
        assertThrows(BlueprintNotFoundException.class,
                () -> persistence.addPoint("ghost", "ghost-bp", 1, 1));
    }
}