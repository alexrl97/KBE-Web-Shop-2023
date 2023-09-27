describe('SignUp', () => {
    it('should create a user and log in with random credentials', () => {
        const randomEmail = generateRandomEmail();
        const randomPassword = generateRandomPassword();

        // Visit the sign-up page
        cy.visit('http://localhost:8081/signUp');

        const constantFirstName = 'John';
        const constantLastName = 'Doe';
        const constantCompany = 'Example Company';
        const constantStreet = 'Main Street';
        const constantHouseNumber = '123';
        const constantZip = '12345';
        const constantCity = 'Example City';
        const constantCountry = 'Example Country';


        // Fill in the sign-up form with constant and random values
        cy.get('input[type="email"]').type(randomEmail);
        cy.get('input[type="text"]').eq(1).type(constantFirstName);
        cy.get('input[type="text"]').eq(2).type(constantLastName);
        cy.get('input[type="text"]').eq(3).type(constantCompany);
        cy.get('input[type="text"]').eq(4).type(constantStreet);
        cy.get('input[type="text"]').eq(5).type(constantHouseNumber);
        cy.get('input[type="text"]').eq(6).type(constantZip);
        cy.get('input[type="text"]').eq(7).type(constantCity);
        cy.get('input[type="text"]').eq(8).type(constantCountry);
        cy.get('input[type="password"]').eq(0).type(randomPassword);
        cy.get('input[type="password"]').eq(1).type(randomPassword);

        // Submit the sign-up form
        cy.contains('button', 'Erstellen').click();

        // Assert successful registration swal
        cy.get('.swal-text').should('contain', 'Registrierung erfolgreich');

        // Navigate to the sign-in page
        cy.visit('http://localhost:8081/signIn');

        // Fill in the sign-in form with the generated credentials
        cy.get('input[type="email"]').type(randomEmail);
        cy.get('input[type="password"]').type(randomPassword);
        cy.contains('button', 'Weiter').click();

        // Assert successful login swal
        cy.get('.swal-text').should('contain', 'Anmelden erfolgreich');

        // Click the "OK" button in the swal
        cy.get('.swal-button-container').click();

        // Assert that the URL has been changed to the main page
        cy.url().should('eq', 'http://localhost:8081/');
    });

    it('storehouse user can create new storehouse user', () => {

        const randomEmail = generateRandomEmail();
        const randomPassword = generateRandomPassword();
        const constantFirstName = 'John';
        const constantLastName = 'Doe';

        cy.visit('http://localhost:8081/signIn');

        cy.get('input[type="email"]').type('storehouse@mail.de');
        cy.get('input[type="password"]').type('storehouse');
        cy.contains('button', 'Weiter').click();

        cy.get('.swal-text').should('contain', 'Anmelden erfolgreich');

        cy.get('.swal-button-container').click();

        cy.url().should('eq', 'http://localhost:8081/');

        cy.wait(500);

        cy.get('.nav-link').contains('Konto').click();

        cy.get('.dropdown-menu').contains('Mitarbeiter').click();

        cy.get('input[type="email"]').type(randomEmail);
        cy.get('input[type="text"]').eq(1).type(constantFirstName);
        cy.get('input[type="text"]').eq(2).type(constantLastName);
        cy.get('input[type="password"]').eq(0).type(randomPassword);
        cy.get('input[type="password"]').eq(1).type(randomPassword);

        cy.contains('button', 'Erstellen').click();
        cy.get('.swal-text').should('contain', 'Registrierung erfolgreich');

        cy.visit('http://localhost:8081/signIn');

        cy.get('input[type="email"]').type(randomEmail);
        cy.get('input[type="password"]').type(randomPassword);
        cy.contains('button', 'Weiter').click();

        // Assert successful login swal
        cy.get('.swal-text').should('contain', 'Anmelden erfolgreich');
    });
});

// Helper functions to generate random values
function generateRandomEmail() {
    return `test${Math.random().toString(36).substring(7)}@example.com`;
}

function generateRandomPassword() {
    return Math.random().toString(36).substring(7);
}