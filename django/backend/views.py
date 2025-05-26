from django.http import JsonResponse
from rest_framework import viewsets
from rest_framework.decorators import action
from rest_framework.response import Response
from rest_framework import status
from drf_spectacular.utils import extend_schema

from backend.models import MenuItem, Order
from backend.serializers import (
    MenuItemSerializer,
    OrderSerializer,
    UserLoginResponseSerializer,
    UserRegistrationResponseSerializer,
    UserRegistrationSerializer,
    UserLoginSerializer,
)

# Create your views here.

class MenuItemViewSet(viewsets.ViewSet):
    queryset = MenuItem.objects.all()
    serializer_class = MenuItemSerializer

    @action(detail=False, methods=['get'], url_path='get-all-categories')
    def get_all_categories(self, request):
        categories = MenuItem.objects.values_list('category', flat=True).distinct()
        return Response({'categories': list(categories)})
    
    @action(detail=False, methods=['get'], url_path='items-by-category')
    def items_by_category(self, request):
        category = request.query_params.get('category')
        if not category:
            return Response({'error': 'Category parameter is required'}, status=status.HTTP_400_BAD_REQUEST)
        
        items = MenuItem.objects.filter(category=category).distinct()
        serializer = MenuItemSerializer(items, many=True)
        return Response(serializer.data)
    
    def list(self, request):
        items = MenuItem.objects.all()
        serializer = MenuItemSerializer(items, many=True)
        return Response(serializer.data)


class OrderViewSet(viewsets.ViewSet):
    queryset = Order.objects.all()
    serializer_class = OrderSerializer

    @action(detail=False, methods=['get'], url_path='orders-by-user')
    def orders_by_user(self, request):
        user_id = request.query_params.get('user_id')
        if not user_id:
            return Response({'error': 'User ID parameter is required'}, status=status.HTTP_400_BAD_REQUEST)
        
        orders = Order.objects.filter(user_id=user_id).order_by('-order_date')
        serializer = OrderSerializer(orders, many=True)
        return Response(serializer.data)
    
    @action(detail=False, methods=['post'], url_path='create-order')
    def create_order(self, request):
        serializer = OrderSerializer(data=request.data)
        if serializer.is_valid():
            order = serializer.save()
            return Response({
                'message': 'Order created successfully',
                'order_id': order.id,
                'total_amount': str(order.total_amount),
                'status': order.status,
            }, status=status.HTTP_201_CREATED)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

    @action(detail=False, methods=['get'], url_path='latest-order')
    def latest_order(self, request):
        latest = Order.objects.order_by('-order_date').first()
        if latest:
            return Response({'timestamp': latest.order_date.isoformat()})
        return Response({'timestamp': None})

class UserViewSet(viewsets.ViewSet):

    @extend_schema(
        request=UserRegistrationSerializer,
        responses=UserRegistrationResponseSerializer
    )
    @action(detail=False, methods=['post'])
    def register(self, request):
        serializer = UserRegistrationSerializer(data=request.data)
        if serializer.is_valid():
            user = serializer.save()
            return Response({
                'message': 'User created successfully',
                'username': user.username,
                'first_name': user.first_name,
                'last_name': user.last_name,
            }, status=status.HTTP_201_CREATED)
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

    @extend_schema(
        request=UserLoginSerializer,
        responses=UserLoginResponseSerializer
    )
    @action(detail=False, methods=['post'])
    def login(self, request):
        serializer = UserLoginSerializer(data=request.data)
        if serializer.is_valid():
            user = serializer.validated_data
            return Response(
                {
                    'username': user.username,
                    'first_name': user.first_name,
                    'last_name': user.last_name,
                },
                status=status.HTTP_202_ACCEPTED
            )
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)
    