<template>
    <div class="container">
        <div class="row">
            <div class="col-12 text-center" style="margin-top: 10px;">
                <h4>Neue Karte hinzufügen</h4>
            </div>
        </div>
        <div class="row">
            <div class="col-3"></div>
            <div class="col-6">
                <form>
                    <div class="form-group">
                        <label>Kartentyp</label>
                        <select class="form-control" v-model="categoryId" required>
                            <option v-for="category in categories" :key="category.id"
                                    :value="category.id">{{ category.categoryName }}</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <label>Name</label>
                        <input type="text" v-model="name" class="form-control" >
                    </div>
                    <div class="form-group">
                        <label>Beschreibung</label>
                        <input type="text" v-model="description" class="form-control" >
                    </div>
                    <div class="form-group">
                        <label>Bild URL</label>
                        <input type="text"  v-model="imageURL" class="form-control" >
                    </div>
                    <div class="form-group">
                      <label>Karten ID</label>
                      <input type="text"  v-model="deckCardId" class="form-control" >
                    </div>
                    <div class="form-group">
                        <label>Preis</label>
                        <input type="number" lang="en" step=".01"  v-model="price" class="form-control" >
                    </div>
                    <button type="button" class="btn btn-primary" @click="addProduct">Produkt hinzufügen</button>
                </form>
            </div>
            <div class="col-3"></div>
        </div>

<!--        Form-->
    </div>
</template>
<script>
    import axios from 'axios'
    import swal from 'sweetalert'
    export default {
        props: ["baseURL", "categories"],
        data() {
            return {
                id: null,
                categoryId: null,
                name: null,
                description: null,
                imageURL: null,
                price: null,
                deckCardId: null,
            }
        },
        methods: {
            addProduct() {
                const newProduct = {
                    categoryId: this.categoryId,
                    description: this.description,
                    name: this.name,
                    imageURL: this.imageURL,
                    price: this.price,
                    deckCardId: this.deckCardId
                };

                axios.post(this.baseURL+"product/add?token="+this.token, newProduct)
                .then(() => {
                    swal({
                        text: "Produkt hinzugefügt",
                        icon: "success"
                    }).then(() => {
                      this.$emit("fetchData");
                      this.$router.push({name: 'AdminProduct'});
                    });
                }).catch((err)=> {
                    console.log("err", err);
                })
            }
        },
      mounted() {
        this.token = localStorage.getItem("token");
      },
    }
</script>