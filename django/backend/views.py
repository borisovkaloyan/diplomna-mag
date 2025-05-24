from django.http import JsonResponse
from rest_framework import viewsets
from rest_framework.decorators import action
from rest_framework.response import Response
from rest_framework import status

from backend.models import MenuItem, Order
from backend.serializers import (
    MenuItemSerializer,
    OrderSerializer,
    UserRegistrationSerializer,
    UserLoginSerializer,
)

# Create your views here.

class MenuItemViewSet(viewsets.ModelViewSet):
    queryset = MenuItem.objects.all()
    serializer_class = MenuItemSerializer

class OrderViewSet(viewsets.ModelViewSet):
    queryset = Order.objects.all()
    serializer_class = OrderSerializer

    @action(detail=False, methods=['get'], url_path='latest-order')
    def latest_order(self, request):
        latest = Order.objects.order_by('-order_date').first()
        if latest:
            return Response({'timestamp': latest.order_date.isoformat()})
        return Response({'timestamp': None})

class UserViewSet(viewsets.ViewSet):
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

    @action(detail=False, methods=['post'])
    def login(self, request):
        serializer = UserLoginSerializer(data=request.data)
        if serializer.is_valid():
            user = serializer.validated_data
            return Response({
                'username': user.username,
                'first_name': user.first_name,
                'last_name': user.last_name,
            })
        return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)
    