import { getRoleFromToken } from '@/helper/getRoleFromToken.js';
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
import ConferenceView from "@/views/ConferenceView.vue";
import ActiveConferences from "@/views/ActiveConferences.vue";
import EditProfileView from "@/views/EditProfileView.vue";

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
          meta: { requiresStudent: true },
        },
        {
          path: 'main',
          name: 'main',
          component: MainView,
          meta: { requiresStudent: true },
        },
        {
          path: '/student',
          name: 'student',
          component: StudentView,
          meta: { requiresStudent: true },
        },
        {
          path: '/activeConferences',
          name: 'activeConferences',
          component: ActiveConferences,
          meta: { requiresStudent: true },
        },
        {
          path: '/user',
          name: 'EditProfileView',
          component: EditProfileView,
        },
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
      meta: { requiresAdmin: true },
      children: [
        {
          path: 'users',
          name: 'users',
          component: AdminUsersView,
          meta: { requiresAdmin: true },
        },
        {
          path: 'users/:userID',
          name: 'UserDetailView',
          component: UserDetailView,
          props: true,
          meta: { requiresAdmin: true },
        },
        {
          path: 'conference',
          name: 'ConferenceView',
          component: ConferenceView,
          props: true,
          meta: { requiresAdmin: true },
        }
      ],
    }
  ]
});

router.beforeEach((to, from, next) => {
  const userRole = getRoleFromToken();

  if (to.meta.requiresAdmin && userRole !== 'ADMIN') {
    return next('/');
  }

  if (to.meta.requiresStudent && userRole !== 'STUDENT') {
    return next('/admin/users');
  }

  next();
});

export default router