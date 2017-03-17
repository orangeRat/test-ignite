package su.dru.ignite;

import com.google.common.collect.ImmutableList;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;
import su.dru.ignite.domain.CellToNumberRelation;
import su.dru.ignite.domain.ClientProfile;
import su.dru.ignite.service.CellService;
import su.dru.ignite.service.CellToNumberService;
import su.dru.ignite.service.ClientProfileService;

import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * @author Andrey Vorobyov
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class IgniteTest {
    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;
    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private CellService cellService;
    @Autowired
    private CellToNumberService cellToNumberService;
    @Autowired
    private ClientProfileService clientProfileService;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {
        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
                .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
                .findAny()
                .orElse(null);

        assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
    }

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
    }

    @Test
    public void getCellFailTest() throws Exception {
        mockMvc.perform(get("/cell/10"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getProfileFailTest() throws Exception {
        mockMvc.perform(get("/profile/10"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getCtnListFailTest() throws Exception {
        mockMvc.perform(get("/cell/10/numbers"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void addCtnTest() throws Exception {
        String requestBody = json(new CellToNumberRelation(1000L, 80L));
        mockMvc.perform(post("/cell/add-number").content(requestBody).contentType(contentType))
                .andExpect(status().isOk());
    }

    @Test
    public void getCellTest() throws Exception {
        String requestBody = json(new CellToNumberRelation(1001L, 81L));
        mockMvc.perform(post("/cell/add-number").content(requestBody).contentType(contentType))
                .andExpect(status().isOk());

        mockMvc.perform(get("/cell/81"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.cellId", is(81)));
    }

    @Test
    public void addProfileTest() throws Exception {
        LocalDateTime activateDate = LocalDateTime.now();
        String requestBody = json(new ClientProfile(200, "John Smith", "joe@mail.com", activateDate));

        mockMvc.perform(post("/profile").content(requestBody).contentType(contentType))
                .andExpect(status().isOk());

        mockMvc.perform(get("/profile/200"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.ctn", is(200)))
                .andExpect(jsonPath("$.name", is("John Smith")))
                .andExpect(jsonPath("$.email", is("joe@mail.com")))
                .andExpect(jsonPath("$.activateDate", is(formatter.format(activateDate))));
    }

    @Test
    public void batchCtnLoadTest() throws Exception {
        List<CellToNumberRelation> relations = ImmutableList.<CellToNumberRelation>builder()
                .add(new CellToNumberRelation(1101L, 91L))
                .add(new CellToNumberRelation(1102L, 91L))
                .build();

        String payload = json(relations);
        mockMvc.perform(post("/cell/add-number-batch").content(payload).contentType(contentType))
                .andExpect(status().isOk());

        mockMvc.perform(get("/cell/91"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.cellId", is(91)));

        mockMvc.perform(get("/cell/91/numbers"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].ctn", hasItems(1101, 1102)));
    }

    @Test
    public void batchProfileLoadTest() throws Exception {
        LocalDateTime activateDate = LocalDateTime.now();
        List<ClientProfile> payload = ImmutableList.<ClientProfile>builder()
                .add(new ClientProfile(301, "John Smith", "joe@mail.com", activateDate))
                .add(new ClientProfile(302, "Jack Sparrow", "jack@mail.com", activateDate))
                .build();

        mockMvc.perform(post("/profile/batch").content(json(payload)).contentType(contentType))
                .andExpect(status().isOk());

        mockMvc.perform(get("/profile/301"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.ctn", is(301)))
                .andExpect(jsonPath("$.name", is("John Smith")))
                .andExpect(jsonPath("$.email", is("joe@mail.com")))
                .andExpect(jsonPath("$.activateDate", is(formatter.format(activateDate))));

        mockMvc.perform(get("/profile/302"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.ctn", is(302)))
                .andExpect(jsonPath("$.name", is("Jack Sparrow")))
                .andExpect(jsonPath("$.email", is("jack@mail.com")))
                .andExpect(jsonPath("$.activateDate", is(formatter.format(activateDate))));
    }

    @Test
    public void getCtnTest() throws Exception {
        LocalDateTime activateDate = LocalDateTime.now();
        List<ClientProfile> profiles = ImmutableList.<ClientProfile>builder()
                .add(new ClientProfile(1001L, "John Smith", "joe@mail.com", activateDate))
                .add(new ClientProfile(1002L, "Jack Sparrow", "captain@mail.com", activateDate))
                .add(new ClientProfile(1003L, "Bags Bunny", "rabbit@mail.com", activateDate))
                .add(new ClientProfile(1004L, "Clark Kent", "superman@mail.com", activateDate))
                .add(new ClientProfile(1005L, "Neo", "hacker@matrix.com", activateDate))
                .add(new ClientProfile(1006L, "Linus Torvalds", "linus@mail.com", activateDate))
                .build();

        mockMvc.perform(post("/profile/batch").content(json(profiles)).contentType(contentType))
                .andExpect(status().isOk());

        ImmutableList<CellToNumberRelation> relations = ImmutableList.<CellToNumberRelation>builder()
                .add(new CellToNumberRelation(1001L, 82L))
                .add(new CellToNumberRelation(1002L, 83L))
                .add(new CellToNumberRelation(1003L, 83L))
                .add(new CellToNumberRelation(1004L, 83L))
                .add(new CellToNumberRelation(1005L, 83L))
                .add(new CellToNumberRelation(1006L, 83L))
                .build();

        mockMvc.perform(post("/cell/add-number-batch").content(json(relations)).contentType(contentType))
                .andExpect(status().isOk());

        mockMvc.perform(get("/profile/by-cell/83"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.total", is(5)))
                .andExpect(jsonPath("$.results", hasSize(5)))
                .andExpect(jsonPath("$.results[*].ctn", hasItems(1002, 1003, 1004, 1005, 1006)))
                .andExpect(jsonPath("$.results[*].name", hasItems("Jack Sparrow", "Bags Bunny", "Clark Kent", "Neo", "Linus Torvalds")))
                .andExpect(jsonPath("$.results[*].email", hasItems("captain@mail.com", "rabbit@mail.com", "superman@mail.com", "hacker@matrix.com", "linus@mail.com")))
                .andExpect(jsonPath("$.results[0].activateDate", is(formatter.format(activateDate))));
    }

    protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }
}
