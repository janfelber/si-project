import { getUserRole } from '@/helper/getUserRole.js';
import AdminArticleDetailView from '@/views/AdminArticleDetailView.vue';
import AdminArticlesView from '@/views/AdminArticlesView.vue';
import NotFoundView from '@/views/NotFoundView.vue';
import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'
import LoginView from "@/views/LoginView.vue";
import RegisterView from "@/views/RegisterView.vue";
import UploadFile from '@/views/UploadArticle.vue';
import AdminUsersView from "@/views/AdminUsersView.vue";
import UserDetailView from "@/views/UserDetailView.vue";
import ConferenceView from "@/views/ConferenceView.vue";
import ActiveConferences from "@/views/ActiveConferences.vue";
import ConferenceDetail from "@/views/ConferenceDetail.vue";
import EditProfileView from "@/views/EditProfileView.vue";
import GradingView from "@/views/GradingView.vue";
import AdminConferenceDetailView from "@/views/AdminConferenceDetailView.vue";

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
      path: '/admin/articles',
      name: 'AdminArticlesView',
      component: AdminArticlesView,
      meta: {
        requiresAdmin: true,
        title: 'Articles'
      },
    },
    {
      path: '/admin/articles/:id',
      name: 'AdminArticleDetailView',
      component: AdminArticleDetailView,
      props: true,
      meta: {
        requiresAdmin: true,
        title: 'Article Detail'
      },
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
      path: '/admin/conferenceDetail/:id',
      name: 'ConferenceDetail',
      component: AdminConferenceDetailView,
      meta: { requiresAdmin: true },
      props:true
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
      path: '/:catchAll(.*)',
      redirect: '/web/404',
    },
    {
      path: '/',
      redirect: '/login',
    },
    {
      path: '/web/404',
      name: '404',
      component: NotFoundView,
      meta: {
        title: '404'
      }
    },
  ]
});

router.beforeEach(async (to, from, next) => {
  const userRole = await getUserRole();

  if (to.path === '/') {
    if (userRole === 'ADMIN') {
      return next('/admin/users');
    } else if (userRole === 'STUDENT' || userRole === 'REVIEWER') {
      return next('/web/home');
    } else {
      return next('/login');
    }
  }

  if (to.path === '/login' || to.path === '/register') {
    return next();
  }

  if (to.meta.requiresAdmin && userRole !== 'ADMIN') {
    return next('/web/home');
  }

  if (to.meta.requiresStudent && userRole !== 'STUDENT' && userRole !== 'REVIEWER') {
    return next('/admin/users');
  }

  next();
});

export default router;