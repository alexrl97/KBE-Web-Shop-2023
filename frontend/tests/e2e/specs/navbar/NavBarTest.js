describe('NavBar', () => {
    beforeEach(() => {
        cy.visit('http://localhost:8081/');
    });

    it('should display the logo', () => {
        cy.get('#logo').should('be.visible');
    });

    it('should display the search bar', () => {
        cy.get('input[placeholder="Suche Karten"]').should('be.visible');
    });

    it('should have elements in the "Karten" dropdown menu', () => {
        cy.get('.nav-link').contains('Karten').click();
        cy.get('.dropdown-menu').should('contain', 'Kartentypen');
        cy.get('.dropdown-menu').should('contain', 'Karten');
    });

    it('should have elements in the "Konto" dropdown menu', () => {
        cy.get('.nav-link').contains('Konto').click();
        cy.get('.dropdown-menu').should('contain', 'Einloggen');
        cy.get('.dropdown-menu').should('contain', 'Registrieren');
    });

    it('should display the shopping cart symbol', () => {
        cy.get('#cart').should('be.visible');
        cy.get('.fa.fa-shopping-cart').should('be.visible');
    });

    it('should have storehouse elements in the "Konto" dropdown menu after storehouse sign in', () => {

        cy.visit('http://localhost:8081/signIn');

        cy.get('input[type="email"]').type('storehouse@mail.de');
        cy.get('input[type="password"]').type('storehouse');
        cy.contains('button', 'Weiter').click();

        cy.get('.swal-text').should('contain', 'Anmelden erfolgreich');

        cy.get('.swal-button-container').click();

        cy.url().should('eq', 'http://localhost:8081/');

        cy.wait(500);

        cy.get('.nav-link').contains('Konto').click();

        cy.get('.dropdown-menu').should('contain', 'Bestellungen');
        cy.get('.dropdown-menu').should('contain', 'Mitarbeiter');
        cy.get('.dropdown-menu').should('contain', 'Abmelden');
    });

    it('should have customer elements in the "Konto" dropdown menu after customer sign in', () => {

        cy.visit('http://localhost:8081/signIn');

        cy.get('input[type="email"]').type('customer@mail.de');
        cy.get('input[type="password"]').type('customer');
        cy.contains('button', 'Weiter').click();

        cy.get('.swal-text').should('contain', 'Anmelden erfolgreich');

        cy.get('.swal-button-container').click();

        cy.url().should('eq', 'http://localhost:8081/');

        cy.wait(500);

        cy.get('.nav-link').contains('Konto').click();
        cy.get('.dropdown-menu').should('contain', 'Wunschliste');
        cy.get('.dropdown-menu').should('contain', 'Bestellungen');
        cy.get('.dropdown-menu').should('contain', 'Abmelden');
    });


});
