<template>

    <div class="card" style="margin-top: 10px;">
      <div class="embed-responsive" style="padding-top: 150%;">
            <img
                class="card-img-top embed-responsive-item"
                :src="product.imageURL"
                alt="Card image cap"
            />
        </div>
        <div class="card-body">
            <router-link :to="{name: 'ShowDetails', params: {id : product.id}}">
                <h5 class="card-title" style="font-size: 75%">{{ product.name }}</h5>
            </router-link>
            <p class="card-text" style="font-size: 50%;">
                {{ product.description }}
            </p>

            <p class="price" style=" margin-left: 50%; margin-bottom: 0%">
              {{ product.price }}&euro;
            </p>
            <router-link :to="{name: 'EditProduct', params: {id: product.id}}"
            v-show="$route.name === 'AdminProduct'">
                <button v-if="role === 'storehouse'" id="button" class="btn btn-primary" style="margin-bottom: 0%">Bearbeiten</button>
            </router-link>

        </div>
    </div>
</template>
<script>
    export default {
        name: "ProductBox",
        props: ["product"],

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
<style scoped>
    .card-img-top {
        object-fit: cover;
    }
    a {
        text-decoration: none;
    }
    .card-title {
        color: #484848;
    }

    .price {
      position: absolute;
      bottom: 5px;
      right: 5px;
      font-size: 50%;
      background-color: #fff;
      padding: 5px 10px;
      border-radius: 5px;
      border: 1px solid black; /* Add black border around the text */
    }

    .btn-primary {
      position: absolute;
      bottom: 5px;
      left: 5px;
      font-size: 50%;
      padding: 5px 10px;
    }


</style>