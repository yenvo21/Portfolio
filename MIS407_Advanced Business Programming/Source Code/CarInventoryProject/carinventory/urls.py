"""carinventory URL Configuration

The `urlpatterns` list routes URLs to views. For more information please see:
    https://docs.djangoproject.com/en/4.0/topics/http/urls/
Examples:
Function views
    1. Add an import:  from my_app import views
    2. Add a URL to urlpatterns:  path('', views.home, name='home')
Class-based views
    1. Add an import:  from other_app.views import Home
    2. Add a URL to urlpatterns:  path('', Home.as_view(), name='home')
Including another URLconf
    1. Import the include() function: from django.urls import include, path
    2. Add a URL to urlpatterns:  path('blog/', include('blog.urls'))
"""
# Import admin module
from django.contrib import admin
# Import path module
from django.template.defaulttags import url
from django.urls import path, re_path
# --------------------
from django.conf.urls.static import static
from django.conf import settings
from django.contrib import admin
from django.contrib.staticfiles.urls import staticfiles_urlpatterns


#---------------------
# Import view
from django.views.generic import TemplateView

from inventory import views

urlpatterns = [

    path('admin/', admin.site.urls),
    path('', TemplateView.as_view(template_name='index.html'), name='home'),
    path('home/', TemplateView.as_view(template_name='index.html'), name='home'),
    # Inventory List
    path('inventory/', views.inventory, name='inventory'),
    path('inventory/add', views.inventory_add, name='add_inventory'),
    path('inventory/brand', views.brand_view, name='brand_view'),
    # Searching
    path('search/', views.search_inventory, name='search_inventory'),
    path('<int:id>/', views.inventory_detail, name='inventory_detail'),
    path('inventory/delete/<int:id>', views.inventory_delete, name='inventory_delete'),
    path('inventory/edit/<int:id>', views.inventory_edit, name='inventory_edit'),
    path('inventory/update/<int:id>', views.inventory_update, name='inventory_update'),
]

urlpatterns += staticfiles_urlpatterns()
