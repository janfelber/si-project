import { getUserRole } from '@/helper/getUserRole.js';
import NoPermissions from '@/views/NoPermissions.vue';
import AdminArticleDetailView from '@/views/AdminArticleDetailView.vue';
import AdminArticlesView from '@/views/AdminArticlesView.vue';
import NotFoundView from '@/views/NotFoundView.vue';
import ReviewsArticle from '@/views/ReviewsArticle.vue';
import UpdateFile from '@/views/UpdateFile.vue';
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
import ReviewView from "@/views/ReviewView.vue";
import AdminConferenceDetailView from "@/views/AdminConferenceDetailView.vue";
import AdminCategoriesView from "@/views/AdminCategoriesView.vue";
import ArticleHistory from "@/views/ArticleHistory.vue";
import ArticleReviewResponse from "@/views/ArticleReviewResponse.vue";
import ArticleReviewResponseAdmin from '@/views/ArticleReviewResponseAdmin.vue';

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
        title: 'Zoznam všetkých používateľov'
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
      meta: {
        requiresAdmin: true,
        title: 'Konferencie'
      },
    },
    {
      path: '/admin/articles',
      name: 'AdminArticlesView',
      component: AdminArticlesView,
      meta: {
        requiresAdmin: true,
        title: 'Odovzdané práce'
      },
    },
    {
      path: '/admin/articles/:id',
      name: 'AdminArticleDetailView',
      component: AdminArticleDetailView,
      props: true,
      meta: {
        requiresAdmin: true,
        title: 'Detail práce'
      },
    },
    {
      path: '/admin/categories',
      name: 'AdminCategoriesView',
      component: AdminCategoriesView,
      meta: {
        requiresAdmin: true,
        title: 'Kategórie'
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
      meta: {
        requiresStudent: true,
        title: 'Nahraj prácu'
      },
    },
    {
      path: '/web/update/:id',
      name: 'update',
      component: UpdateFile,
      props: true,
      meta: {
        requiresStudent: true,
        title: 'Úprava práce'
      },
    },
    {
      path: '/admin/conferenceDetail/:id',
      name: 'ConferenceDetail',
      component: AdminConferenceDetailView,
      meta: { requiresAdmin: true,
              title: 'Detail konferencie'
      },
      props:true
    },
    {
      path: '/reviewArticle/:id',
      component: ReviewView,
      props: true,
      meta: {
        requiresReviewer: true,
        title: 'Úprava práce'
      },
      children: [
        {
          path: '/reviewArticle/:id',
          name: 'reviewArticle',
          component: ReviewView,
        },
      ],
    },
    {
      path: '/web/active-conferences',
      name: 'activeConferences',
      component: ActiveConferences,
      meta: {
        requiresStudent: true,
        title: 'Aktívne konferencie'
      },
    },
    {
      path: '/web/review-article',
      name: 'reviewConferences',
      component: ReviewsArticle,
      meta: {
        requiresReviewer: true,
        title: 'Recenzia prác'
      },
    },
    {
      path: '/web/history-articles',
      name: 'ArticleHistory',
      component: ArticleHistory,
      meta: {
        requiresStudent: true,
        title: 'História tvojich prác'
      },
    },
    {
      path: '/web/article-review/:id',
      name: 'ArticleReviewResponse',
      component: ArticleReviewResponse,
      props: true,
      meta: {
        name: 'ArticleReviewResponse',
        requiresStudent: true,
        title: 'Reakcia na recenziu práce'
      },
    },
    {
      path: '/web/article-review-admin/:id',
      name: 'ArticleReviewResponseAdmin',
      component: ArticleReviewResponseAdmin,
      props: true,
      meta: {
        name: 'ArticleReviewResponseAdmin',
        requiresAdmin: true,
        title: 'Hodnotenie práce'
      },
    },
    {
      path: '/web/no-permission',
      name: 'noReviewerPermission',
      component: NoPermissions,
      meta: {
        title: 'No Permission'
      }
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

  if ((to.meta.requiresAdmin || to.meta.requiresStudent || to.meta.requiresReviewer) && !userRole) {
    return next('/login');
  }

  if (!userRole && to.path !== '/login' && to.path !== '/register') {
    return next('/login');
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

  if (to.meta.requiresReviewer && userRole !== 'REVIEWER') {
    return next('/web/no-permission');
  }

  next();
});

export default router;