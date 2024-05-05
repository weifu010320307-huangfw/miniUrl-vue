describe('template spec', () => {
  it('passes', () => {


    cy.visit('/')


    cy.contains("miniUrl")

    cy.url().should("include", '/')
  })
})