describe('Category', () => {

    beforeEach(() => {
        cy.visit('http://localhost:8081/signIn');

        cy.get('input[type="email"]').type('storehouse@mail.de');
        cy.get('input[type="password"]').type('storehouse');
        cy.contains('button', 'Weiter').click();
        cy.get('.swal-button-container').click();
    });

    it('storehouse user can access category creation', () => {
        cy.visit('http://localhost:8081');
        cy.get('.nav-link').contains('Karten').click();
        cy.get('.dropdown-item').eq(0).click();
        cy.url().should('eq', 'http://localhost:8081/category');
        cy.contains('button', 'Kartentyp hinzufügen').click();
        cy.url().should('eq', 'http://localhost:8081/category/add');
    });

    it('storehouse user can create category', () => {
        cy.visit('http://localhost:8081/category/add');
        const randomName = `Category_${Math.floor(Math.random() * 1000)}`;
        const randomDescription = `Description_${Math.floor(Math.random() * 1000)}`;
        const imageURL = 'https://i.ibb.co/x5NRzkg/4.webp';

        cy.get('input[type="text"]').eq(1).type(randomName);
        cy.get('input[type="text"]').eq(2).type(randomDescription);
        cy.get('input[type="text"]').eq(3).type(imageURL);

        cy.get('button').contains('Kartentyp hinzufügen').click();

        cy.get('.swal-text').should('contain', 'Kartentyp hinzugefügt');
        cy.get('.swal-button').contains('OK').click();
        cy.url().should('eq', 'http://localhost:8081/category');

        // Locate all the ProductBox components on the page
        cy.get('.card-title').each(($title) => {
            cy.wrap($title).invoke('text').then((text) => {
                if (text.includes(randomName)) {
                    cy.log(`Found ProductBox with name: ${randomName}`);
                }
            });
        });
    });

    it('storehouse user can access category edit', () => {
        cy.visit('http://localhost:8081');
        cy.get('.nav-link').contains('Karten').click();
        cy.get('.dropdown-item').eq(0).click();
        cy.url().should('eq', 'http://localhost:8081/category');
        cy.contains('button', 'Bearbeiten').click();
        cy.url().should('contain', 'http://localhost:8081/category/');
    });

    it('storehouse user can edit category', () => {
        cy.visit('http://localhost:8081');
        cy.get('.nav-link').contains('Karten').click();
        cy.get('.dropdown-item').eq(0).click();
        cy.url().should('eq', 'http://localhost:8081/category');
        cy.contains('button', 'Bearbeiten').click();
        const randomDescription = `Description_${Math.floor(Math.random() * 1000)}`;
        cy.get('input[type="text"]').eq(2).clear();
        cy.get('input[type="text"]').eq(2).type(randomDescription);
        cy.contains('button', 'Speichern').click();
        cy.get('.swal-text').should('contain', 'Kartentyp aktualisiert');
        cy.get('.swal-button').contains('OK').click();
        cy.url().should('eq', 'http://localhost:8081/category');
        cy.get('.card-text').each(($description) => {
            cy.wrap($description).invoke('text').then((text) => {
                if (text.includes(randomDescription)) {
                    cy.log(`Found ProductBox with name: ${randomDescription}`);
                }
            });
        });
    });

    it('storehouse user can delete category', () => {
        cy.visit('http://localhost:8081');
        cy.get('.nav-link').contains('Karten').click();
        cy.get('.dropdown-item').eq(0).click();
        cy.url().should('eq', 'http://localhost:8081/category');
        cy.get('button:contains("Bearbeiten")').should('be.visible').should('be.enabled').then(($buttons) => {
            // Click the fourth "Bearbeiten" button (index 3)
            cy.wrap($buttons[3]).click();
        });
        cy.contains('button', 'Löschen').click();
        cy.get('.swal-button').contains('OK').click();
        cy.url().should('eq', 'http://localhost:8081/category');
    });
});