from rest_framework.routers import DefaultRouter

from backend.views import MenuItemViewSet, OrderViewSet, UserViewSet

router = DefaultRouter()

router.register(r'menu-items', MenuItemViewSet, basename='menuitem')
router.register(r'orders', OrderViewSet, basename='order')
router.register(r'users', UserViewSet, basename='user')
