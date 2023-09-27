describe('WishList', () => {

    beforeEach(() => {
        cy.visit('http://localhost:8081/signIn');

        cy.get('input[type="email"]').type('customer@mail.de');
        cy.get('input[type="password"]').type('customer');
        cy.contains('button', 'Weiter').click();
        cy.get('.swal-button-container').click();
    });

    it('customer can access wishlist', () => {
        cy.visit('http://localhost:8081');
        cy.get('.nav-link').contains('Konto').click();
        cy.get('.dropdown-item').contains('Wunschliste').click();
        cy.url().should('eq', 'http://localhost:8081/wishlist');
    });

    it('customer can add product to wishlist', () => {
        cy.visit('http://localhost:8081/product');
        cy.get('.card-title').last().click();
        cy.url().should('contain', 'http://localhost:8081/product/');
        cy.contains('button', 'Wunschliste hinzufügen').click();
        cy.get('.swal-text').should('contain', 'Zur Wunschliste hinzugefügt');
        cy.get('.swal-button').contains('OK').click();
        cy.visit('http://localhost:8081/wishlist');
        cy.get('.col-lg-2.col-md-3.col-sm-4.col-6.pt-3.justify-content-around.d-flex')
            .should('have.length', 1);
    });

    it('customer can remove item from wishlist', () => {
        cy.visit('http://localhost:8081/wishlist');
        cy.contains('button', 'Löschen').click();
        cy.get('.swal-text').should('contain', 'Karte von der Wunschliste entfernt');
        cy.get('.swal-button').contains('OK').click();
        cy.get('.col-lg-2.col-md-3.col-sm-4.col-6.pt-3.justify-content-around.d-flex')
            .should('not.exist');
    });
});