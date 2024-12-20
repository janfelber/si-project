import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import LoginView from "@/views/LoginView.vue";
import MainView from "@/views/MainView.vue";
import RegisterView from "@/views/RegisterView.vue";
import DefaultView from "@/views/DefaultView.vue";
import AuthLayout from "@/views/AuthLayout.vue";
import StudentView from '@/views/StudentView.vue';
import AdminLayout from "@/views/AdminLayout.vue";
import AdminUsersView from "@/views/AdminUsersView.vue";
import UserDetailView from "@/views/UserDetailView.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      component: DefaultView,
      children: [
        {
          path: '',
          name: 'home',
          component: HomeView,
        },
        {
          path: 'main',
          name: 'main',
          component: MainView,
        },
        {
          path: '/student',
          name: 'student',
          component: StudentView
        }
      ],
    },
    {
      path: '/login',
      component: AuthLayout,
      children: [
        {
          path: '',
          name: 'login',
          component: LoginView,
        },
      ],
    },
    {
      path: '/register',
      component: AuthLayout,
      children: [
        {
          path: '',
          name: 'register',
          component: RegisterView,
        },
      ],
    },
    {
      path: '/admin',
      component: AdminLayout,
      children: [
        {
          path: 'users',
          name: 'users',
          component: AdminUsersView,
        },
        {
          path: 'users/:userID',
          name: 'UserDetailView',
          component: UserDetailView,
          props: true,
        }
      ],
    },
  ]
})

export default router