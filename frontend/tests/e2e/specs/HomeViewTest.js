describe('HomeView', () => {
  beforeEach(() => {
    cy.visit('http://localhost:8081/');
  });

  it('should display categories', () => {
    cy.get('.col-12.text-center h2').first().should('contain', 'Kartentypen');
    cy.get('.col-md-6.col-xl-4.col-12.pt-3.justify-content-around.d-flex').should('exist');
  });

  it('should display products', () => {
    cy.get('.col-12.text-center h2').last().should('contain', 'Karten');
    cy.get('.col-lg-2.col-md-3.col-sm-4.col-6.pt-3.justify-content-around.d-flex').should('exist');
  });
});
