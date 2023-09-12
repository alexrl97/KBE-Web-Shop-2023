import { createRouter, createWebHistory } from 'vue-router';
import Home from '../views/HomeView.vue';
import AddCategory from '../views/Category/AddCategory';
import Category from '../views/Category/Category';
import Product from '../views/Product/Product';
import AddProduct from '../views/Product/AddProduct';
import EditCategory from '../views/Category/EditCategory';
import EditProduct from '../views/Product/EditProduct';
import ShowDetails from '../views/Product/ShowDetails';
import ListProducts from '../views/Category/ListProducts';
import WishList from '../views/Product/WishList';
import Signup from '../views/Signup';
import Signin from '../views/Signin';
import Cart from '../views/cart/Cart';
import Success from '../views/payment/Success';
import Failed from '../views/payment/Failed';
import Checkout from '../views/Checkout/Checkout';
import OrderHistory from "../views/order/OrderHistory";
import OrderDetails from "../views/order/OrderDetails";

const routes = [
  {
    path: '/',
    name: 'Home',
    component: Home,
  },
  {
    path: '/category/show/:id',
    name: 'ListProducts',
    component: ListProducts,
  },
  {
    path: '/category/add',
    name: 'AddCategory',
    component: AddCategory,
  },

  {
    path: "/category",
    name: "AdminCategory",
    component: Category,
  },

  // category edit
  {
  path: '/category/:id',
  name: 'EditCategory',
  component: EditCategory,
  },
  {
    path: '/product',
    name: 'AdminProduct',
    component: Product,
  },
    // add product
  {
    path: '/product/new',
    name: 'AddProduct',
    component: AddProduct,
  },
  {
    path: '/product/:id',
    name: 'EditProduct',
    component: EditProduct,
  },
  {
    path: '/product/show/:id',
    name: 'ShowDetails',
    component: ShowDetails,
  },

  {
    path: '/signup',
    name: 'Signup',
    component: Signup,
  },
  {
    path: '/signin',
    name: 'Signin',
    component: Signin,
  },
  {
    path: '/wishlist',
    name: 'WishList',
    component: WishList,
  },
  {
      path: '/cart',
      name: 'Cart',
      component: Cart,
  },
  {
    path: '/payment/success',
    name: 'PaymentSuccess',
    component: Success,
  },

  {
    path: '/payment/failed',
    name: 'PaymentFailed',
    component: Failed,
  },

  {
    path: '/checkout',
    name: 'Checkout',
    component: Checkout,
  },
  {
    path: '/orders',
    name: 'OrderHistory',
    component: OrderHistory
  },
  {
    path:'/order/:id',
    name:'OrderDetails',
    component: OrderDetails
  }

]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

export default router;