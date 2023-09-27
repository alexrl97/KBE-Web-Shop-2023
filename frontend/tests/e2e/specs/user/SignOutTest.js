describe('SignOut', () => {
    it('customer should be able to log out after login', () => {
        cy.visit('http://localhost:8081/signIn');

        cy.get('input[type="email"]').type('customer@mail.de');
        cy.get('input[type="password"]').type('customer');
        cy.contains('button', 'Weiter').click();

        cy.get('.swal-text').should('contain', 'Anmelden erfolgreich');

        cy.get('.swal-button-container').click();

        cy.url().should('eq', 'http://localhost:8081/');

        cy.wait(500);

        cy.get('.nav-link').contains('Konto').click();
        cy.get('.dropdown-item').contains('Abmelden').click();

        cy.get('.swal-text').should('contain', 'Ausloggen erfolgreich');
        cy.get('.swal-button').contains('OK').click();

        cy.url().should('eq', 'http://localhost:8081/signIn');
    });
});