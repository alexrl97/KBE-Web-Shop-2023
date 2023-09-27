describe('Cart', () => {

    beforeEach(() => {
        cy.visit('http://localhost:8081/signIn');

        cy.get('input[type="email"]').type('customer@mail.de');
        cy.get('input[type="password"]').type('customer');
        cy.contains('button', 'Weiter').click();
        cy.get('.swal-button-container').click();
    });

    it('customer can access cart', () => {
        cy.visit('http://localhost:8081');
        cy.get('div#cart i.fa.fa-shopping-cart').click({ force: true });
        cy.url().should('eq', 'http://localhost:8081/cart');
    });

    it('customer can add product to cart', () => {
        cy.visit('http://localhost:8081/product');
        cy.get('.card-title').last().click();
        cy.url().should('contain', 'http://localhost:8081/product/');
        cy.contains('button', 'Warenkorb hinzufügen').click();
        cy.get('.swal-text').should('contain', 'Karte dem Warenkorb hinzugefügt');
        cy.get('.swal-button').contains('OK').click();
        cy.get('div#cart i.fa.fa-shopping-cart').click({ force: true });
        cy.url().should('eq', 'http://localhost:8081/cart');
        cy.get('.col-md-5.px-3').should('have.length', 1);
    });

    it('customer can remove item from cart', () => {
        cy.visit('http://localhost:8081/cart');
        cy.get('.row.mt-2.pt-3.justify-content-around') // Locate the container for each cart item
            .first() // Select the first cart item
            .within(() => {
                cy.contains('Aus dem Warenkorb entfernen').click(); // Click the link to remove the item
            });
        cy.get('.col-md-5.px-3').should('not.exist');
    });
});