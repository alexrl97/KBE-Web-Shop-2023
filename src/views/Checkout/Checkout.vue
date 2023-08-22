<template>
    <div class="div_class">
      <h3>Du wirst zum Stripe Bezahlsystem weitergeleitet</h3>
      <div class="alert alert-primary">
        Das Bezahlsystem befindet sich im Testmodus. Verwende 4242 4242 4242 4242 als Kreditkartennummer
        und beliebige Angaben für eine erfolgreiche Zahlung.
      </div>
  
      <button class="btn btn-primary" @click="goToCheckout">Bezahlen</button>
    </div>
  </template>
  
  <script>
  import axios from 'axios';
  export default {
    data() {
      return {
        stripeAPIToken: 'pk_test_51NSKMwKJzXoLk7hctXfhMTHr5Dt2PCBi4G4DgcduQgelpcweZ7zUJikr7ybQsVFpYuTnRzYHqwTN87VckBqN2Suh005X4Vdj3h',
        stripe: '',
        token: null,
        checkoutBodyArray: [],
      };
    },
    name: 'Checkout',
    props: ['baseURL'],
    methods: {
      includeStripe( URL, callback ){
        let documentTag = document, tag = 'script',
            object = documentTag.createElement(tag),
            scriptTag = documentTag.getElementsByTagName(tag)[0];
        object.src = '//' + URL;
        if (callback) { object.addEventListener('load', function (e) { callback(null, e); }, false); }
        scriptTag.parentNode.insertBefore(object, scriptTag);
      },

      configureStripe(){
        /* global Stripe */
        this.stripe = Stripe( this.stripeAPIToken );
      },


      getAllItems() {
        axios
          .get(`${this.baseURL}cart/?token=${this.token}`)
          .then((response) => {
            if (response.status == 200) {
              let products = response.data;
              for (let i = 0; i < products.cartItems.length; i++) {
                this.checkoutBodyArray.push({
                  price: products.cartItems[i].product.price,
                  quantity: products.cartItems[i].quantity,
                  productName: products.cartItems[i].product.name,
                  productId: products.cartItems[i].product.id,
                });
              }
            }
          })
          .catch((err) => console.log(err));
      },
      goToCheckout() {
        console.log('checkoutBodyArray', this.checkoutBodyArray);
        axios
          .post(
            `${this.baseURL}order/create-checkout-session`,
            this.checkoutBodyArray
          )
          .then((response) => {
            localStorage.setItem('sessionId', response.data.sessionId);
            console.log('session', response.data);
            this.stripe.redirectToCheckout({
              sessionId: response.data.sessionId,
            });
          })
          .catch((err) => console.log(err));
      },
    },
    mounted() {
      this.token = localStorage.getItem('token');

      this.includeStripe('js.stripe.com/v3/', function(){
        this.configureStripe();
      }.bind(this) );
      this.getAllItems();
    },
  };
  </script>
  <style scoped>
  .alert {
    width: 50%;
  }
  .div_class {
    margin-top: 5%;
    margin-left: 30%;
  }
  .checkout_button {
    background-color: #5d3dec;
    margin: 10px;
  }
  </style>