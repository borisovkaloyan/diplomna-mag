from django.http import JsonResponse
from rest_framework import viewsets
from rest_framework.decorators import action
from rest_framework.response import Response
from rest_framework import status
from drf_spectacular.utils import extend_schema

from backend.models import MenuItem, Order, OrderItem
from backend.serializers import (
    MenuItemSerializer,
    OrderRequestSerializer,
    OrderSerializer,
    OrdersByUserSerializer,
    UserLoginResponseSerializer,
    UserRegistrationResponseSerializer,
    UserRegistrationSerializer,
    UserLoginSerializer,
    OrderCategorySerializer
)

# Create your views here.

class MenuItemViewSet(viewsets.ViewSet):
    queryset = MenuItem.objects.all()
    serializer_class = MenuItemSerializer

    @action(detail=False, methods=['get'], url_path='get-all-categories')
    def get_all_categories(self, request):
        categories = MenuItem.objects.values_list('category', flat=True).distinct()
        return Response({'categories': list(categories)})
    
    @extend_schema(
        request=OrderCategorySerializer,
        responses=MenuItemSerializer(many=True)
    )
    @action(detail=False, methods=['post'], url_path='items-by-category')
    def items_by_category(self, request):
        serializer = OrderCategorySerializer(data=request.data)
        
        if serializer.is_valid():
            category = serializer.validated_data.get('category')
        else:
            return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)

        items = MenuItem.objects.filter(category=category).distinct()
        serializer = MenuItemSerializer(items, many=True)
        return Response(serializer.data)
    
    def list(self, request):
        items = MenuItem.objects.all()
        serializer = MenuItemSerializer(items, many=True)
        return Response(serializer.data)
    
    @action(detail=True, methods=['get'], url_path='get-item-by-id')
    def get_item_by_id(self, request, pk='id'):
        item_id = pk
        if not item_id:
            return Response({'error': 'Item ID is required'}, status=status.HTTP_400_BAD_REQUEST)
        
        try:
            item = MenuItem.objects.get(id=item_id)
            serializer = MenuItemSerializer(item)
            return Response(serializer.data)
        except MenuItem.DoesNotExist:
            return Response({'error': 'Item not found'}, status=status.HTTP_404_NOT_FOUND)


class OrderViewSet(viewsets.ViewSet):
    queryset = Order.objects.all()
    serializer_class = OrderSerializer

    @extend_schema(
        request=OrdersByUserSerializer,
        responses=OrderSerializer(many=True)
    )
    @action(detail=False, methods=['post'], url_path='orders-by-user')
    def orders_by_user(self, request):

        serializer = OrdersByUserSerializer(data=request.data)
        if not serializer.is_valid():
            return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)
        
        user_id = serializer.validated_data.get('user_id')
        
        orders = Order.objects.filter(user_id=user_id).order_by('-order_date')
        serializer = OrderSerializer(orders, many=True)
        return Response(serializer.data)
    
    @extend_schema(
        request=OrderRequestSerializer,
        responses=OrderSerializer
    )
    @action(detail=False, methods=['post'], url_path='create-order')
    def create_order(self, request):
        serializer = OrderRequestSerializer(data=request.data)
        if serializer.is_valid():
            order = OrderSerializer(data=serializer.validated_data)
            if order.is_valid():
                order = order.save()
                response = OrderSerializer(order).data
                return Response(response, status=status.HTTP_201_CREATED)
            else:
                return Response(order.errors, status=status.HTTP_400_BAD_REQUEST)
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
                    'user_id': user.id,
                    'username': user.username,
                    'first_name': user.first_name,
                    'last_name': user.last_name,
                },
                status=status.HTTP_202_ACCEPTED
            )
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)
