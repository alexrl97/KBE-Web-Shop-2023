<template>
    <div class="container">
        <div class="row">
            <div class="col-12 text-center" style="margin-top: 10px;">
                <h4 class="pt-3"> Karte bearbeiten</h4>
            </div>
        </div>
        <div class="row">
            <div class="col-3"></div>
            <div class="col-6">
                <form v-if="product">
                    <div class="form-group">
                        <label> Kartentyp</label>
                        <select class="form-control" v-model="product.categoryId" required>
                            <option v-for="category of categories"
                                    :key="category.id"
                                    :value="category.id"> {{category.categoryName}}</option>

                        </select>
                    </div>
                    <div class="form-group">
                        <label>Name</label>
                        <input type="text" class="form-control" v-model="product.name" required/>
                    </div>
                    <div class="form-group">
                        <label>Beschreibung</label>
                        <input type="text" class="form-control" v-model="product.description" required/>
                    </div>
                    <div class="form-group">
                        <label>Bild URL</label>
                        <input type="text" class="form-control" v-model="product.imageURL" required/>
                    </div>
                    <div class="form-group">
                      <label>Karten ID</label>
                      <input type="text" class="form-control" v-model="product.deckCardId" required/>
                    </div>
                    <div class="form-group">
                        <label>Preis</label>
                        <input type="number" lang="en" step=".01" class="form-control" v-model="product.price" required/>
                    </div>
                  <div class="d-flex justify-content-between"> <!-- Use the d-flex class to create a flex container and justify-content-between to align items at the ends -->
                    <button type="button" class="btn btn-primary" @click="editProduct">Speichern</button>
                    <button type="button" class="btn btn-primary" style="background-color: red" @click="deleteProduct">Löschen</button>
                  </div>
                </form>
            </div>
            <div class="col-3"></div>
        </div>
    </div>
</template>
<script>
    import axios from 'axios'
    import swal from 'sweetalert'
    export default {
        data() {
            return {
                product: null,
                id: null
            }
        },
        props: ["baseURL", "categories", "products"],
        methods: {
            async editProduct() {
                await axios.post(`${this.baseURL}product/update/${this.id}?token=${this.token}`,this.product)
                    .then(() => {
                        swal({
                            text: "Produkt aktualisiert",
                            icon: "success"
                        }).then(() => {
                          this.$emit("fetchData");
                          this.$router.push({name: 'AdminProduct'})
                        });
                    }).catch(err => console.log('err', err));
            },
          async deleteProduct() {
            console.log('product', this.product)
            await axios.delete(`${this.baseURL}product/delete/${this.id}?token=${this.token}`,
                this.product)
                .then(() => {
                  swal({
                    text: "Produkt gelöscht",
                    icon: "success"
                  }).then(() => {
                    this.$emit("fetchData");
                    this.$router.push({name: 'AdminProduct'})
                  });
                }).catch(err => console.log('err', err));
          }
        },
        mounted() {
            this.id = this.$route.params.id;
            this.product = this.products.find(product => product.id == this.id)
            this.token = localStorage.getItem("token");
        }
    }
</script>