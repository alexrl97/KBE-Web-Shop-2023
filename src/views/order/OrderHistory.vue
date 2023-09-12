<template>
  <div class="container">
    <div class="row">
      <div class="col-12 text-center" style="margin-top: 10px">
        <h4 class="pt-3">Bestellungen</h4>
      </div>
    </div>
    <!-- for each order display -->
    <div v-for="order in orderList" :key="order.pid" class="row mt-2 pt-3 justify-content-around">
      <div class="col-2"></div>
      <div class="col-md-3 embed-responsive embed-responsive-16by9">
        <!-- display image on the left -->
        <img v-bind:src="order.imageURL" class="w-100 card-img-top embed-responsive-item">
      </div>
      <div class="col-md-5 px-3">
        <div class="card-block px-3">
          <h6 class="card-title">
            <router-link v-bind:to="'/order/'+order.id">Bestellung #{{order.id}}</router-link>
          </h6>
          <p class="mb-0">{{order.totalItems}} Karte<span v-if="order.totalItems > 1">n</span></p>
          <p id="item-price" class="mb-0 font-weight-bold">Gesamtpreis: {{order.totalCost}}€</p>
          <p id="item-total-price">Bestelldatum: {{order.orderdate}}</p>
          <p id="item-total-price">Status:
            <span v-if="order.status === 'send'">Versendet</span>
            <span v-else>In Bearbeitung</span>
          </p>
          <a v-if="order.status === 'send'" v-bind:href="order.trackingLink">
            Sendungsverfolgung
          </a>
        </div>
      </div>
      <div class="col-md-2">
        <div v-if="order.status !== 'send' && role === 'storehouse'" >
          <input
              v-model="order.trackingNumber"
              type="text"
              placeholder="Sendungsnummer"
          />
          <button @click="sendOrder(order.id, order.trackingNumber)">
            Versenden
          </button>

        </div>

          <div v-if="role === 'customer'">
          <br>Versandaddresse:
          <br>
          </div>
        <p>
          <br>{{order.firstName}} {{order.lastName}}
          <br v-if="order.company !== ''" >{{order.company}}
          <br>{{order.street}} {{order.houseNumber}}
          <br>{{order.zip}} {{order.city}}
          <br>{{order.country}}
        </p>
      </div>
      <div class="col-12"><hr></div>
    </div>
  </div>
</template>

<script>
import swal from "sweetalert";
const axios = require('axios')
export default {

  data() {
    return {
      token: null,
      role: null,
      orderList : []
    }
  },
  props:["baseURL"],
  name: 'Order',
  methods: {
    // list of order histories
    listOrders(){
      axios.get(`${this.baseURL}order/?token=${this.token}`)
          .then((response) => {
                if(response.status==200){
                  this.orders = response.data
                  // for each order populate orderList
                  this.orders.forEach((order) => {
                    this.orderList.push({
                      id: order.id,
                      totalCost: order.totalPrice,
                      // get short date
                      orderdate: order.createdDate.substring(0,10),
                      status: order.status,
                      // get image of the first orderItem of the order
                      imageURL: order.orderItems[0].product.imageURL,
                      // get total items
                      totalItems: order.orderItems.length,
                      trackingNumber: order.trackingNumber,
                      trackingLink: "https://www.dhl.de/de/privatkunden/dhl-sendungsverfolgung.html?piececode=".concat(order.trackingNumber),

                      firstName: order.address.firstName,
                      lastName: order.address.lastName,
                      company: order.address.company,
                      street: order.address.street,
                      houseNumber: order.address.houseNumber,
                      zip: order.address.zip,
                      city: order.address.city,
                      country: order.address.country,
                    })
                  })
                }
              },
              (error)=>{
                console.log(error)
              });
    },

    sendOrder(orderID, trackingNumber) {
      axios
          .post(`${this.baseURL}order/send/${orderID}?token=${this.token}&trackingNumber=${trackingNumber}`)
          .then((response) => {
            console.log('Bestellung versendet:', response.data);
            swal({
              text: "Bestellung versendet",
              icon: "success",
            }).then(() => {
              this.orderList = [];
              this.listOrders();
            });
          })
          .catch((error) => {
            console.error('Fehler beim Versenden der Bestellung:', error);
          });
    },
  },
  mounted() {
    this.token = localStorage.getItem("token");
    this.role = localStorage.getItem("role");
    this.listOrders();
  },
};

</script>

<style scoped>
h4, h5 {
  font-family: 'Roboto', sans-serif;
  color: #484848;
  font-weight: 700;
}

.embed-responsive .card-img-top {
  object-fit: cover;
}
</style>
