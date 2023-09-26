import { createRouter, createWebHistory } from 'vue-router';
import Home from '../views/HomeView.vue';
import AddCategory from '../views/category/AddCategory';
import Category from '../views/category/Category';
import Product from '../views/product/Product';
import AddProduct from '../views/product/AddProduct';
import EditCategory from '../views/category/EditCategory';
import EditProduct from '../views/product/EditProduct';
import ShowDetails from '../views/product/ShowDetails';
import SearchProduct from "@/views/product/SearchProduct";
import ListProducts from '../views/category/ListProducts';
import WishList from '../views/product/WishList';
import SignUp from '../views/user/SignUp';
import SignIn from '../views/user/SignIn';
import Cart from '../views/cart/Cart';
import Success from '../views/payment/Success';
import Failed from '../views/payment/Failed';
import Checkout from '../views/checkout/Checkout';
import OrderHistory from "../views/order/OrderHistory";
import OrderDetails from "../views/order/OrderDetails";
import EditAddress from "../views/address/EditAddress";

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
    path: '/product/search/:name/:categoryId',
    name: 'SearchProduct',
    component: SearchProduct,
  },
  {
    path: '/signUp',
    name: 'SignUp',
    component: SignUp,
  },
  {
    path: '/signIn',
    name: 'SignIn',
    component: SignIn,
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
  },
  {
    path:'/address',
    name:'EditAddress',
    component: EditAddress
  }

]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes,
});

export default router;