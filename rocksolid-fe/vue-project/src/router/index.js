import { getRoleFromToken } from '@/helper/getRoleFromToken.js';
import { createRouter, createWebHistory } from 'vue-router'
import { jwtDecode } from "jwt-decode";
import HomeView from '../views/HomeView.vue'
import LoginView from "@/views/LoginView.vue";
import MainView from "@/views/MainView.vue";
import RegisterView from "@/views/RegisterView.vue";
import AuthLayout from "@/views/AuthLayout.vue";
import UploadFile from '@/views/UploadArticle.vue';
import AdminUsersView from "@/views/AdminUsersView.vue";
import UserDetailView from "@/views/UserDetailView.vue";
import ConferenceView from "@/views/ConferenceView.vue";
import ActiveConferences from "@/views/ActiveConferences.vue";
import ConferenceDetail from "@/views/ConferenceDetail.vue";
import EditProfileView from "@/views/EditProfileView.vue";
import GradingView from "@/views/GradingView.vue";

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/login',
      name: 'login',
      component: LoginView,
    },
    {
      path: '/register',
      name: 'register',
      component: RegisterView,
    },
    {
      path: '/admin/users',
      name: 'users',
      component: AdminUsersView,
      meta: {
        requiresAdmin: true,
        title: 'Users'
      },
    },
    {
      path: '/admin/users/:userID',
      name: 'UserDetailView',
      component: UserDetailView,
      props: true,
      meta: { requiresAdmin: true },
    },
    {
      path: '/admin/conference',
      name: 'ConferenceView',
      component: ConferenceView,
      meta: { requiresAdmin: true },
    },
    {
      path: '/web/home',
      name: 'home',
      component: HomeView,
      meta: { requiresStudent: true },
    },
    {
      path: '/web/upload/:id',
      name: 'upload',
      component: UploadFile,
      props: true,
      meta: { requiresStudent: true },
    },
    {
      path: '/grade',
      component: GradingView,
      children: [
        {
          path: '',
          name: 'grade',
          component: GradingView,
        },
      ],
    },
    {
      path: '/web/active-conferences',
      name: 'activeConferences',
      component: ActiveConferences,
      meta: {
        requiresStudent: true,
        title: 'Active Conferences'
      },
    },
    {
      path: '/web/user',
      name: 'EditProfileView',
      component: EditProfileView,
      meta: {
        requiresStudent: true,
        title: 'Edit Profile'
      }
    },
    {
      path: '/web/conference/:id',
      name: 'conferenceDetail',
      component: ConferenceDetail,
      props: true,
      meta: {
        requiresStudent: true,
        // TODO title should be the conference name
      },
    },
    {
      path: '/',
      redirect: (to) => {
        const userRole = getRoleFromToken();
        if (userRole === 'ADMIN') {
          return '/admin/users';
        } else if (userRole === 'STUDENT') {
          return '/web/home';
        } else {
          return '/login';
        }
      },
    },
    {
      path: '/:catchAll(.*)',
      redirect: (to) => {
        const userRole = getRoleFromToken();
        if (userRole === 'ADMIN') {
          return '/admin/users';
        } else if (userRole === 'STUDENT') {
          return '/web/home';
        } else {
          return '/login';
        }
      },
    },
    {
      path: '/',
      redirect: '/login',
    },
  ]
});

router.beforeEach((to, from, next) => {
  const userRole = getRoleFromToken();
  console.log(userRole);

  if (!userRole) {
    return next('/login');
  }

  if (to.path === '/login' || to.path === '/register') {
    return next();
  }

  if (to.meta.requiresAdmin && userRole !== 'ADMIN') {
    return next('/');
  }

  if (to.meta.requiresStudent && userRole !== 'STUDENT') {
    return next('/admin/users');
  }

  next();
});

export default router