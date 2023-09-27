describe('SignIn', () => {
    it('should log in with valid customer credentials', () => {
        cy.visit('http://localhost:8081/signIn');

        cy.get('input[type="email"]').type('customer@mail.de');
        cy.get('input[type="password"]').type('customer');
        cy.contains('button', 'Weiter').click();

        cy.get('.swal-text').should('contain', 'Anmelden erfolgreich');

        cy.get('.swal-button-container').click();

        cy.url().should('eq', 'http://localhost:8081/');
    });

    it('should log in with valid storehouse credentials', () => {
        cy.visit('http://localhost:8081/signIn');

        cy.get('input[type="email"]').type('storehouse@mail.de');
        cy.get('input[type="password"]').type('storehouse');
        cy.contains('button', 'Weiter').click();

        cy.get('.swal-text').should('contain', 'Anmelden erfolgreich');

        cy.get('.swal-button-container').click();

        cy.url().should('eq', 'http://localhost:8081/');
    });

    it('should display an error message for invalid credentials', () => {
        cy.visit('http://localhost:8081/signIn');

        cy.get('input[type="email"]').type('test@test.de');
        cy.get('input[type="password"]').type('test');
        cy.contains('button', 'Weiter').click();

        cy.get('.swal-text').should('contain', 'Anmelden fehlgeschlagen');
    });
});