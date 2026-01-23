import {createRouter, createWebHashHistory, createWebHistory} from "vue-router";
import {getToken} from '@/utils/auth'

export const constantRoutes = [
    // {
    //     path: '/redirect',
    //     hidden: true,
    //     children: [
    //         {
    //             path: '/redirect/:path(.*)',
    //             component: () => import('@/views/redirect/index.vue')
    //         }
    //     ]
    // },
    {
        path: '/login',
        component: () => import('@/views/login.vue'),
        hidden: true
    },
    {
        path: '/register',
        component: () => import('@/views/register.vue'),
        hidden: true
    },
    {
        path: "/",
        redirect: '/index',
    },
    {
        path: '/index',
        name: '首页',
        component: () => import('@/views/index.vue'),

    },
    {
        path: '/allType/index',
        name: 'AllType',
        component: () => import('@/views/allType/index'),
    }, {
        path: '/contentDetail/index/:id',
        name: 'detial',
        component: () => import('@/views/contentDetail/index'),
    }, {
        path: '/order/index',
        name: 'orderIndex',
        component: () => import('@/views/order/index'),
        meta: {requiresAuth: true},

    }, {
        path: '/order/payMethod',
        name: 'PayMethod',
        component: () => import('@/views/order/payMethod'),
        meta: {requiresAuth: true},

    }, {
        path: '/order/paySuccess',
        name: 'PaySuccess',
        component: () => import('@/views/order/paySuccess'),
        meta: {requiresAuth: true},

    },{
        path: '/order/buyTicketUser',
        name: 'BuyTicketUser',
        component: () => import('@/views/order/buyTicketUser'),
        meta: {requiresAuth: true},

    },{
        path: '/order/seatSelect',
        name: 'SeatSelect',
        component: () => import('@/views/order/seatSelect'),
        meta: {requiresAuth: true},

    }, {
        path: '/personInfo/index',
        name: '个人信息',
        component: () => import('@/views/personInfo/index'),
        meta: {requiresAuth: true}
    },{
        path: '/personInfo/ticketUser',
        name: '购票人列表',
        component: () => import('@/views/personInfo/ticketUser.vue'),
        meta: {requiresAuth: true}
    }, {
        path: '/accountSettings/index',
        name: '账号设置',
        component: () => import('@/views/accountSettings/index'),
        meta: {requiresAuth: true}
    }, {
        path: '/orderManagement/index',
        name: 'OrderManagement',
        component: () => import('@/views/orderManagement/index'),
        meta: {requiresAuth: true}
    }, {
        path: '/orderManagement/orderDetail/:orderNumber',
        // path: '/orderManagement/orderDetail',
        name: 'orderDetail',
        component: () => import('@/views/orderManagement/orderDetail.vue'),
        meta: {requiresAuth: true}
    }, {
        path: '/accountSettings/editPassword',
        name: '修改密码',
        component: () => import('@/views/accountSettings/components/editPassword'),
        meta: {requiresAuth: true}
    },
    {
        path: '/accountSettings/email',
        name: '邮箱验证',
        component: () => import('@/views/accountSettings/components/email'),
        meta: {requiresAuth: true}
    },
    {
        path: '/accountSettings/mobile',
        name: '手机验证',
        component: () => import('@/views/accountSettings/components/mobile'),
        meta: {requiresAuth: true}
    },
    {
        path: '/accountSettings/authentication',
        name: '实名认证',
        component: () => import('@/views/accountSettings/components/authentication'),
        meta: {requiresAuth: true}
    },
    {
        path: '/demo/index',
        name: 'demo',
        component: () => import('@/views/demo/index'),
    },
]
const router = createRouter({
    history: createWebHistory(),
    // history: createWebHashHistory(),//线上
    routes: constantRoutes,
    scrollBehavior(to, from, savedPosition) {
        if (savedPosition) {
            return savedPosition
        } else {
            return {top: 0}
        }
    },
})

router.beforeEach((to, from, next) => {

    if (to.matched.some(record => record.meta.requiresAuth) && !getToken()) {
        next('/login') // 如果用户未登录，则重定向到登录页
    } else {
        next() // 否则，正常导航到目标页面
    }
})

export default router
