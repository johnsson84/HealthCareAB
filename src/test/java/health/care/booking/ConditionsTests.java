package health.care.booking;

import health.care.booking.dto.CreateCondition;
import health.care.booking.models.Conditions;
import health.care.booking.models.ConditionsCategory;
import health.care.booking.respository.ConditionsRepository;
import health.care.booking.services.ConditionsService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ConditionsTests {

    @Mock
    private ConditionsRepository conditionsRepository;

    @InjectMocks
    private ConditionsService conditionsService;

    @Test
    public void shouldCreateACondition() throws Exception {
        // Arrange
        Conditions savedCondition = new Conditions();
        savedCondition.setId("1");
        savedCondition.setName("Fever");
        savedCondition.setDescription("Massive pain in head.");
        savedCondition.setCategory(ConditionsCategory.DISORDERS);

        CreateCondition createCondition = new CreateCondition();
        createCondition.setName("Fever");
        createCondition.setDescription("Massive pain in head.");
        createCondition.setCategory("disorders");

        when(conditionsRepository.save(any())).thenReturn(savedCondition);

        // Act
        Conditions createdCondition = conditionsService.createCondition(createCondition);

        // Assert
        assertEquals("Fever", createdCondition.getName(), "Name should match");
        assertEquals("Massive pain in head.", createdCondition.getDescription(), "Description should match");
        assertEquals(ConditionsCategory.DISORDERS, createdCondition.getCategory(), "Category should match");
        System.out.println("Success! A condition was created");
    }

    @Test
    public void shouldFailBecauseWrongCategory() throws Exception {
        // Arrange
        Conditions savedCondition = new Conditions();
        savedCondition.setId("1");
        savedCondition.setName("Fever");
        savedCondition.setDescription("Massive pain in head.");
        savedCondition.setCategory(ConditionsCategory.DISORDERS);

        CreateCondition createCondition = new CreateCondition();
        createCondition.setName("Fever");
        createCondition.setDescription("Massive pain in head.");
        createCondition.setCategory("healthy");

        // Act
        Exception exception = assertThrows(Exception.class, () -> {
            conditionsService.createCondition(createCondition);
        });

        // Assert
        assertEquals("Invalid category: healthy", exception.getMessage());
        System.out.println("Success! failed on category check");
    }
}
