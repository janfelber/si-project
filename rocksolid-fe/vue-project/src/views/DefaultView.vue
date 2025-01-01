<template>
  <div class="default-layout">
    <header class="header">
      <nav class="header-nav">
        <div class="active-router">
          <span class="router-label">Users</span>
        </div>
        <div class="user-item">
          <span>{{ routeName }} </span>
        </div>
      </nav>
      <div class="vertical-divider"></div>
      <div class="spacer"></div>
      <div class="vertical-divider"></div>
      <div class="user-container">
        <div class="user-menu">
          <button class="user-menu-button" @click="toggleDropdown">
            <div class="user-avatar">
            </div>
            <div class="user-info">
              <div class="user-details">
                <span class="name">{{ fullName }}</span>
                <span class="email">{{ email }}</span>
              </div>
              <img v-if="universityLogo" :src="universityLogo" alt="University Logo" class="university-logo" />
            </div>
          </button>
          <div class="dropdown-content" v-if="isDropdownOpen">
            <div class="wrapper">
              <router-link
                  to="/user"
                  class="user-detail"
                  active-class="active"
                  exact
              >
                <span style="font-size: 12px">Profil</span>
                <v-icon size="small">mdi-account</v-icon>
              </router-link>
            </div>
            <hr class="devider">
            <div class="wrapper">
              <a href="" class="logout">
                <span style="font-size: 12px">Odhlásiť sa</span>
                <v-icon size="small">mdi-exit-to-app</v-icon>
              </a>
            </div>

          </div>
        </div>
      </div>
    </header>
    <div class="content">
      <side-nav layout="default"/>
      <main class="main">
        <router-view/>
      </main>
    </div>
  </div>
</template>

<script>
import SideNav from "@/components/Sidenav.vue";
import axios from 'axios';
import {RouterView} from "vue-router";
import router from '../router/index.js';

export default {
  name: "DefaultLayout",
  data() {
    return {
      isDropdownOpen: false,
      firstName: '',
      lastName: '',
      email:'',
      fullName: '',
      universityName: ''
    }
  },
  methods: {
    router() {
      return router
    },
    toggleDropdown() {
      this.isDropdownOpen = !this.isDropdownOpen;
      console.log('Dropdown is now', this.isDropdownOpen ? 'open' : 'closed');
    },
    async fetchUser() {
      try {
        const userResponse = await axios.get('http://localhost:8080/api/v1/user/me', {
          headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`,
          }
        });
        this.fullName = userResponse.data.first_name + ' ' + userResponse.data.last_name;
        this.email = userResponse.data.email;
        this.universityName = userResponse.data.university;
        console.log('User:', userResponse.data.university);
      } catch (error) {
        console.error("Failed to fetch user:", error);
      }
  methods: {
    router() {
      return router
    }
  },
  computed: {
    routeName() {
      return this.$route.meta.title || '';
    },
    universityLogo() {
      switch (this.universityName) {
        case 'UCM':
          return '/university-logo/UCM.png';
        case 'UMB':
          return '/university-logo/UMB.png';
        case 'UKF':
          return '/university-logo/UKF.png';
        default:
          return '/university-logo/default-logo.png';
      }
    },
  },
  created() {
    this.fetchUser();
  },
  components: {RouterView, SideNav},
};
</script>

<style scoped>
.main {
  margin-left: 250px;
  padding: 20px;
}

.devider {
  margin: 0.25rem 0;
  border-top: 1px solid #d8d8f0;
  width: 100%;
}

hr {
  border: 0;
  border-top: 1px solid darkgrey;
  margin-bottom: .75rem;
  margin-top: .75rem;
}

.user-item {
  padding-left : 9rem;
}

label {
  width: 10rem;
  align-self: center;
}

.header-nav {
  margin-left: 1.75rem;
  display: flex;
}

.vertical-divider {
  width: 1px;
  height: 35px;
  background-color: #d8d8f0;
  margin: 0 1rem;
}

header {
  background-color: white;
  display: flex;
  height: 60px;
  align-items: center;
  border-bottom: 1px solid #d8d8f0;
}

.spacer {
  flex-grow: 1;
}

.university-logo {
  height: 50px;
  border-radius: 50%;
  object-fit: cover;
  padding-left: 5rem;
  margin-left: auto;
}

.user-menu-button {
  align-items: center;
  border-radius: .25rem;
  display: flex;
  padding: .25rem;
  width: 16rem;
  border-color: transparent;
  background-color: white;
  box-sizing: border-box;

  &:hover {
    background-color: rgba(228, 226, 226, 0.918);
  }
}

.dropdown-content {
  position: absolute;
  background-color: white;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  z-index: 1;
  padding: .25rem 0;
  gap: .25rem;
  border-radius: .25rem;
  width: 16rem;
}

.wrapper {
  padding: 0 .25rem;
}

.user-detail{
  border-radius: .25rem;
  align-items: center;
  color: #333638;
  display: flex;
  justify-content: space-between;
  padding: .25rem;
  text-decoration: none;

  &:hover {
    background-color: rgba(228, 226, 226, 0.918);
  }
}

.logout {
  border-radius: .25rem;
  align-items: center;
  color: #333638;
  display: flex;
  justify-content: space-between;
  padding: .25rem;
  text-decoration: none;

  &:hover {
    color: #f44336;
    background-color: rgba(244, 67, 54, 0.1);
  }
}


.user-menu {
  margin-right: 2rem;
  position: relative;
}


.user-info {
  display: flex;
  flex: 1;
  flex-direction: row;
  justify-content: flex-start;
  align-items: center;
  max-width: 12rem;
  text-align: start;
}

.user-details {
  display: flex;
  flex-direction: column;
}


.email {
  font-size: 10px;
}

.name {
  font-size: 12px;
  font-weight: bold;
}
</style>