<template>
    <div class="container">
        <div class="row">
            <div class="col-12 text-center" style="margin-top: 10px;">
                <h4> Karten</h4>
                <router-link :to="{name: 'AddProduct'}">
                    <button v-if="role === 'storehouse'" class="btn" style="background-color: #aeffae; margin-top: 30px; margin-bottom: 30px;">
                        Karte hinzufügen
                    </button> </router-link>
            </div>
        </div>
        <div class="row">
            <div v-for="product of products" :key="product.id"
                 class="col-lg-2 col-md-3 col-sm-4 col-6 pt-3 justify-content-around d-flex">
                <ProductBox :product="product"/>
            </div>
        </div>
    </div>
</template>
<script>
    import ProductBox from "../../components/ProductBox";
    export default {
        components: {ProductBox},
        props:["products"],

      data() {
        return {
          role: null,
        };
      },
      mounted() {
        this.role = localStorage.getItem("role");
      },
    }
</script>