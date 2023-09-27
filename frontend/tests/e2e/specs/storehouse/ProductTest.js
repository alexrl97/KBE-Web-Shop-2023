describe('Product', () => {

    beforeEach(() => {
        cy.visit('http://localhost:8081/signIn');

        cy.get('input[type="email"]').type('storehouse@mail.de');
        cy.get('input[type="password"]').type('storehouse');
        cy.contains('button', 'Weiter').click();
        cy.get('.swal-button-container').click();
    });

    it('storehouse user can access product creation', () => {
        cy.visit('http://localhost:8081');
        cy.get('.nav-link').contains('Karten').click();
        cy.get('.dropdown-item').eq(1).click();
        cy.url().should('eq', 'http://localhost:8081/product');
        cy.contains('button', 'Karte hinzufügen ').click();
        cy.url().should('eq', 'http://localhost:8081/product/new');
    });

    it('storehouse user can create product', () => {
        cy.visit('http://localhost:8081/product/new');
        const randomName = `Product_${Math.floor(Math.random() * 1000)}`;
        const randomDescription = `Description_${Math.floor(Math.random() * 1000)}`;
        const imageURL = 'https://m.media-amazon.com/images/I/913j+A+ldyL.jpg';
        const randomPrice = (Math.random() * 100).toFixed(2);
        const randomDeckCardId = `DeckCard_${Math.floor(Math.random() * 1000)}`;

        // Fill out the form
        cy.get('select.form-control').select('Monsterkarten');
        //cy.get('select.form-control').get('option').eq(0);
        cy.get('input[type="text"]').eq(1).type(randomName);
        cy.get('input[type="text"]').eq(2).type(randomDescription);
        cy.get('input[type="text"]').eq(3).type(imageURL);
        cy.get('input[type="number"]').eq(0).type(randomPrice);
        cy.get('input[type="text"]').eq(4).type(randomDeckCardId);

        // Click the "Produkt hinzufügen" button
        cy.get('button').contains('Produkt hinzufügen').click();

        cy.get('.swal-text').should('contain', 'Produkt hinzugefügt');
        cy.get('.swal-button').contains('OK').click();
        cy.url().should('eq', 'http://localhost:8081/product');

        // Locate all the ProductBox components on the page
        cy.get('.card-title').each(($title) => {
            cy.wrap($title).invoke('text').then((text) => {
                if (text.includes(randomName)) {
                    cy.log(`Found ProductBox with name: ${randomName}`);
                }
            });
        });
    });

    it('storehouse user can access product edit', () => {
        cy.visit('http://localhost:8081');
        cy.get('.nav-link').contains('Karten').click();
        cy.get('.dropdown-item').eq(1).click();
        cy.url().should('eq', 'http://localhost:8081/product');
        cy.contains('button', 'Bearbeiten').click();
        cy.url().should('contain', 'http://localhost:8081/product/');
    });

    it('storehouse user can edit product', () => {
        cy.visit('http://localhost:8081');
        cy.get('.nav-link').contains('Karten').click();
        cy.get('.dropdown-item').eq(1).click();
        cy.url().should('eq', 'http://localhost:8081/product');
        cy.contains('button', 'Bearbeiten').click();
        const randomName = `Product_${Math.floor(Math.random() * 1000)}`;
        cy.get('input[type="text"]').eq(1).clear();
        cy.get('input[type="text"]').eq(1).type(randomName);
        cy.contains('button', 'Speichern').click();
        cy.get('.swal-text').should('contain', 'Produkt aktualisiert');
        cy.get('.swal-button').contains('OK').click();
        cy.url().should('eq', 'http://localhost:8081/product');
        cy.get('.card-title').each(($title) => {
            cy.wrap($title).invoke('text').then((text) => {
                if (text.includes(randomName)) {
                    cy.log(`Found ProductBox with name: ${randomName}`);
                }
            });
        });
    });

    it('storehouse user can delete product', () => {
        cy.visit('http://localhost:8081');
        cy.get('.nav-link').contains('Karten').click();
        cy.get('.dropdown-item').eq(1).click();
        cy.url().should('eq', 'http://localhost:8081/product');
        cy.contains('button', 'Bearbeiten').click();
        const randomName = `Product_${Math.floor(Math.random() * 1000)}`;
        cy.contains('button', 'Löschen').click();
        cy.get('.swal-text').should('contain', 'Produkt gelöscht');
        cy.get('.swal-button').contains('OK').click();
        cy.url().should('eq', 'http://localhost:8081/product');
    });
});