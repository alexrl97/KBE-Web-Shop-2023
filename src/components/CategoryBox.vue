<template>
    <div class="card" style="margin-top: 10px; width: 70%;">
      <div class="embed-responsive" style="padding-top: 150%;">
        <img
          class="card-img-top embed-responsive-item"
          :src="category.imageUrl"
          alt="Card image cap"
        />
      </div>
      <div class="card-body">
        <router-link :to="{name: 'ListProducts', params: {id: category.id} }">
        <h5 class="card-title" style="text-align: center">{{ category.categoryName }}</h5>
      </router-link>
        <p class="card-text" style="font-size: 75%;text-align: center">
          {{ category.description }}
        </p>
        <router-link :to="{name: 'EditCategory', params: {id: category.id}}"
                     v-show="$route.name === 'AdminCategory'">
          <button v-if="role === 'storehouse'" id="button" class="btn btn-primary" style="margin-bottom: 0%">Bearbeiten</button>
        </router-link>
      </div>
    </div>
  </template>
  <script>
  export default {
    name: "CategoryBox",
    props: ["category"],
    data() {
      return {
        role: null,
      };
    },
    methods: {},
    mounted() {
      this.role = localStorage.getItem("role");
    },
  };
  </script>
  
  <style scoped>
  .card-img-top {
    object-fit: cover;
  }
  .btn-primary {
    position: absolute;
    bottom: 5px;
    font-size: 75%;
    width: 60%;
    margin-left: 10%;
    height: 5%;
  }
  </style>