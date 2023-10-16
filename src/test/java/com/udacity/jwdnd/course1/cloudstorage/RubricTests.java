package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

import java.time.Duration;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RubricTests {
    @LocalServerPort
    private int port;

    private WebDriver driver;

    private String urlPart;

    @BeforeAll
    static void beforeAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void beforeEach() {
        this.driver = new ChromeDriver();
        this.urlPart = "http://localhost:" + this.port;
    }

    @AfterEach
    public void afterEach() {
        if (this.driver != null) {
            driver.quit();
        }
    }

    private void doMockSignUp(String firstName, String lastName, String userName, String password){

        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));
        driver.get("http://localhost:" + this.port + "/signup");
        webDriverWait.until(ExpectedConditions.titleContains("Sign Up"));

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputFirstName")));
        WebElement inputFirstName = driver.findElement(By.id("inputFirstName"));
        inputFirstName.click();
        inputFirstName.sendKeys(firstName);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputLastName")));
        WebElement inputLastName = driver.findElement(By.id("inputLastName"));
        inputLastName.click();
        inputLastName.sendKeys(lastName);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
        WebElement inputUsername = driver.findElement(By.id("inputUsername"));
        inputUsername.click();
        inputUsername.sendKeys(userName);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
        WebElement inputPassword = driver.findElement(By.id("inputPassword"));
        inputPassword.click();
        inputPassword.sendKeys(password);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("buttonSignUp")));
        WebElement buttonSignUp = driver.findElement(By.id("buttonSignUp"));
        buttonSignUp.click();

        Assertions.assertEquals("http://localhost:" + this.port + "/login?success", driver.getCurrentUrl());
    }

    private void doLogIn(String userName, String password) {
        // Log in to our dummy account.
        driver.get("http://localhost:" + this.port + "/login");
        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputUsername")));
        WebElement loginUserName = driver.findElement(By.id("inputUsername"));
        loginUserName.click();
        loginUserName.sendKeys(userName);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("inputPassword")));
        WebElement loginPassword = driver.findElement(By.id("inputPassword"));
        loginPassword.click();
        loginPassword.sendKeys(password);

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("login-button")));
        WebElement loginButton = driver.findElement(By.id("login-button"));
        loginButton.click();
        try {
            webDriverWait.until(ExpectedConditions.titleContains("Home"));
        }catch(Exception ignored){}
    }

    private void doLogOut(){
        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));
        driver.get(urlPart + "/home");
        webDriverWait.until(ExpectedConditions.titleContains("Home"));

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("logout")));
        WebElement logOutBtn = driver.findElement(By.id("logout"));
        logOutBtn.click();

        Assertions.assertEquals("http://localhost:" + this.port + "/login?logout", driver.getCurrentUrl());
        driver.get(urlPart + "/home");
        Assertions.assertEquals("http://localhost:" + this.port + "/login", driver.getCurrentUrl());


    }

    @Test
    @Order(1)
    void testFlowTillLogout() {
        doMockSignUp("rubric","test","rubricTest","password");
        doLogIn("rubricTest","password");
        doLogOut();
    }

    @Test
    void testHomeInaccessible(){
        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));
        driver.get(urlPart + "/home");
        webDriverWait.until(ExpectedConditions.titleContains("Login"));

        Assertions.assertEquals(urlPart + "/login", driver.getCurrentUrl());
    }

    @Test
    @Order(2)
    void testNoteCreation(){
        doLogIn("rubricTest","password");

        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));
        driver.get(urlPart + "/home");
        webDriverWait.until(ExpectedConditions.titleContains("Home"));

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
        WebElement noteTabBtn = driver.findElement(By.id("nav-notes-tab"));
        noteTabBtn.click();

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("addNote")));
        WebElement addNoteBtn = driver.findElement(By.id("addNote"));
        addNoteBtn.click();

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
        WebElement noteTitleTextArea = driver.findElement(By.id("note-title"));
        noteTitleTextArea.click();
        noteTitleTextArea.sendKeys("Title text");

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-description")));
        WebElement noteDescTextArea = driver.findElement(By.id("note-description"));
        noteDescTextArea.click();
        noteDescTextArea.sendKeys("Desc text");

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("saveChanges")));
        WebElement saveChangesBtn = driver.findElement(By.id("saveChanges"));
        saveChangesBtn.click();

        Assertions.assertEquals(urlPart + "/result?status=0", driver.getCurrentUrl());
        driver.get(urlPart + "/home");
        webDriverWait.until(ExpectedConditions.titleContains("Home"));

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
        noteTabBtn = driver.findElement(By.id("nav-notes-tab"));
        noteTabBtn.click();

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title-display")));
        WebElement noteTitleDisplay = driver.findElement(By.id("note-title-display"));
        WebElement noteDescDisplay = driver.findElement(By.id("note-description-display"));

        Assertions.assertEquals("Title text",noteTitleDisplay.getText());
        Assertions.assertEquals("Desc text", noteDescDisplay.getText());
    }

    @Test
    @Order(3)
    void testChangeNote(){
        doLogIn("rubricTest","password");

        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));
        driver.get(urlPart + "/home");
        webDriverWait.until(ExpectedConditions.titleContains("Home"));

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
        WebElement noteTabBtn = driver.findElement(By.id("nav-notes-tab"));
        noteTabBtn.click();

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("editNote")));
        WebElement noteEditBtn = driver.findElement(By.id("editNote"));
        noteEditBtn.click();

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title")));
        WebElement noteTitleTextArea = driver.findElement(By.id("note-title"));
        noteTitleTextArea.click();
        noteTitleTextArea.sendKeys("Title text");

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-description")));
        WebElement noteDescTextArea = driver.findElement(By.id("note-description"));
        noteDescTextArea.click();
        noteDescTextArea.sendKeys("Desc text");

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("saveChanges")));
        WebElement saveChangesBtn = driver.findElement(By.id("saveChanges"));
        saveChangesBtn.click();

        driver.get(urlPart + "/home");
        webDriverWait.until(ExpectedConditions.titleContains("Home"));

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
        noteTabBtn = driver.findElement(By.id("nav-notes-tab"));
        noteTabBtn.click();

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("note-title-display")));
        WebElement noteTitleDisplay = driver.findElement(By.id("note-title-display"));
        WebElement noteDescDisplay = driver.findElement(By.id("note-description-display"));

        Assertions.assertEquals("Title textTitle text",noteTitleDisplay.getText());
        Assertions.assertEquals("Desc textDesc text", noteDescDisplay.getText());
    }

    @Test
    @Order(4)
    void testNoteDelete(){
        doLogIn("rubricTest","password");

        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));
        driver.get(urlPart + "/home");
        webDriverWait.until(ExpectedConditions.titleContains("Home"));

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
        WebElement noteTabBtn = driver.findElement(By.id("nav-notes-tab"));
        noteTabBtn.click();

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("deleteNote")));
        WebElement noteDeleteBtn = driver.findElement(By.id("deleteNote"));
        noteDeleteBtn.click();

        Assertions.assertEquals(urlPart + "/result?status=0", driver.getCurrentUrl());
        driver.get(urlPart + "/home");
        webDriverWait.until(ExpectedConditions.titleContains("Home"));

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-notes-tab")));
        noteTabBtn = driver.findElement(By.id("nav-notes-tab"));
        noteTabBtn.click();

        Assertions.assertTrue(driver.findElements(By.id("note-title-display")).isEmpty());
    }

    @Test
    @Order(5)
    void testCredCreation(){
        doLogIn("rubricTest","password");

        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));
        driver.get(urlPart + "/home");
        webDriverWait.until(ExpectedConditions.titleContains("Home"));

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
        WebElement credTabBtn = driver.findElement(By.id("nav-credentials-tab"));
        credTabBtn.click();

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("addCred")));
        WebElement addCredBtn = driver.findElement(By.id("addCred"));
        addCredBtn.click();

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")));
        WebElement credUrlTextArea = driver.findElement(By.id("credential-url"));
        credUrlTextArea.click();
        credUrlTextArea.sendKeys("test.url");

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-username")));
        WebElement credUsernameTextArea = driver.findElement(By.id("credential-username"));
        credUsernameTextArea.click();
        credUsernameTextArea.sendKeys("test user");

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-password")));
        WebElement credPassTextArea = driver.findElement(By.id("credential-password"));
        credPassTextArea.click();
        credPassTextArea.sendKeys("test pass");

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialSubmitBtn")));
        WebElement saveChangesBtn = driver.findElement(By.id("credentialSubmitBtn"));
        saveChangesBtn.click();

        driver.get(urlPart + "/home");
        webDriverWait.until(ExpectedConditions.titleContains("Home"));

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
        credTabBtn = driver.findElement(By.id("nav-credentials-tab"));
        credTabBtn.click();

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url-display")));
        WebElement credUrlDisplay = driver.findElement(By.id("credential-url-display"));
        WebElement credUsernameDisplay = driver.findElement(By.id("credential-username-display"));
        WebElement credPassDisplay = driver.findElement(By.id("credential-password-display"));

        Assertions.assertEquals("test.url", credUrlDisplay.getText());
        Assertions.assertEquals("test user", credUsernameDisplay.getText());
        Assertions.assertEquals("test pass", credPassDisplay.getText());

    }

    @Test
    @Order(6)
    void testCredEdit(){
        doLogIn("rubricTest","password");

        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));
        driver.get(urlPart + "/home");
        webDriverWait.until(ExpectedConditions.titleContains("Home"));

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
        WebElement credTabBtn = driver.findElement(By.id("nav-credentials-tab"));
        credTabBtn.click();

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("editCred")));
        WebElement editCredBtn = driver.findElement(By.id("editCred"));
        editCredBtn.click();

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url")));
        WebElement credUrlTextArea = driver.findElement(By.id("credential-url"));
        credUrlTextArea.click();
        credUrlTextArea.sendKeys("test.url");

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-username")));
        WebElement credUsernameTextArea = driver.findElement(By.id("credential-username"));
        credUsernameTextArea.click();
        credUsernameTextArea.sendKeys("test user");

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-password")));
        WebElement credPassTextArea = driver.findElement(By.id("credential-password"));
        credPassTextArea.click();
        credPassTextArea.sendKeys("test pass");

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credentialSubmitBtn")));
        WebElement saveChangesBtn = driver.findElement(By.id("credentialSubmitBtn"));
        saveChangesBtn.click();

        Assertions.assertEquals(urlPart + "/result?status=0", driver.getCurrentUrl());
        driver.get(urlPart + "/home");
        webDriverWait.until(ExpectedConditions.titleContains("Home"));

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
        credTabBtn = driver.findElement(By.id("nav-credentials-tab"));
        credTabBtn.click();

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("credential-url-display")));
        WebElement credUrlDisplay = driver.findElement(By.id("credential-url-display"));
        WebElement credUsernameDisplay = driver.findElement(By.id("credential-username-display"));
        WebElement credPassDisplay = driver.findElement(By.id("credential-password-display"));

        Assertions.assertEquals("test.urltest.url",credUrlDisplay.getText());
        Assertions.assertEquals("test usertest user", credUsernameDisplay.getText());
        Assertions.assertEquals("test passtest pass", credPassDisplay.getText());
    }

    @Test
    @Order(7)
    void testCredDelete(){
        doLogIn("rubricTest","password");

        WebDriverWait webDriverWait = new WebDriverWait(driver, Duration.ofSeconds(2));
        driver.get(urlPart + "/home");
        webDriverWait.until(ExpectedConditions.titleContains("Home"));

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
        WebElement credTabBtn = driver.findElement(By.id("nav-credentials-tab"));
        credTabBtn.click();

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("deleteCred")));
        WebElement credDeleteBtn = driver.findElement(By.id("deleteCred"));
        credDeleteBtn.click();

        Assertions.assertEquals(urlPart + "/result?status=0", driver.getCurrentUrl());
        driver.get(urlPart + "/home");
        webDriverWait.until(ExpectedConditions.titleContains("Home"));

        webDriverWait.until(ExpectedConditions.visibilityOfElementLocated(By.id("nav-credentials-tab")));
        credTabBtn = driver.findElement(By.id("nav-credentials-tab"));
        credTabBtn.click();

        Assertions.assertTrue(driver.findElements(By.id("credential-url-display")).isEmpty());
    }
}
