describe('Address', () => {

    beforeEach(() => {
        cy.visit('http://localhost:8081/signIn');

        cy.get('input[type="email"]').type('customer@mail.de');
        cy.get('input[type="password"]').type('customer');
        cy.contains('button', 'Weiter').click();
        cy.get('.swal-button-container').click();
    });

    it('customer can access address edit', () => {
        cy.visit('http://localhost:8081');
        cy.get('div#cart i.fa.fa-shopping-cart').click({ force: true });
        cy.url().should('eq', 'http://localhost:8081/cart');
        cy.get('.btn-success.mt-2').click();
        cy.url().should('eq', 'http://localhost:8081/address');
    });

    it('customer can edit address', () => {
        cy.visit('http://localhost:8081/address');
        const randomName = `Name_${Math.floor(Math.random() * 1000)}`;
        cy.get('input[type="text"]').eq(1).clear();
        cy.get('input[type="text"]').eq(1).type(randomName);
        cy.get('.btn-success.mt-2').click();
        cy.get('.swal-text').should('contain', 'Addresse aktualisiert');
        cy.get('.swal-button').contains('OK').click();
        cy.url().should('eq', 'http://localhost:8081/cart');
        cy.visit('http://localhost:8081/address');
        cy.get('input[type="text"]').eq(1).should('have.value', randomName)
    });


});