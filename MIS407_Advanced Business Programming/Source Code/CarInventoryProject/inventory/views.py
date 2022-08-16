# ----------------------------------------------------------------------------
# Subject: MIS407
# Group  : 3
# Author : Yen Vo
# Date   : 04/25/2022
# version: 1.0
# ---------------------------------------------------------------------------
""" Define all Views used by the Inventory system"""
# ---------------------------------------------------------------------------
# Changes
# 04/25/2022  Yen Vo - Created views:
#                              - index
#                              - inventory
#                              - search_inventory
#                              - inventory_detail
#                              - inventory_add
#                              - brand_view
#                              - search
#
# ---------------------------------------------------------------------------


from django.shortcuts import render, redirect

from django.shortcuts import render, get_object_or_404
from django.contrib.auth.decorators import login_required

from .forms import VehicleForm, BrandForm
from .models import Brand, Vehicle

from django.db.models import Q


# Home page
def index(req):
    return render(req, 'inventory/index.html')


# Inventory
@login_required(login_url='/admin/login/')
def inventory(request):
    vehicles = Vehicle.objects.all()

    return render(request, 'inventory.html', {'vehicles': vehicles})


# Define function to display all Vehicles


@login_required(login_url='/admin/login/')
def search_inventory(request):
    if request.method == 'POST':
        query = request.POST.get('search')

        if query == '':
            query = 'None'

        results = Vehicle.objects.filter(
            Q(model_name__icontains=query) | Q(model_year__icontains=query) | Q(price__icontains=query))
        print(results)
        return render(request, 'search_inventory_result.html', {'query': query, 'results': results})

    else:
        vehicle = Vehicle.objects.all()
        return render(request, 'search_inventory.html', {'vehicle': vehicle})


# Define function to display the particular Vehicle
@login_required(login_url='/admin/login/')
def inventory_detail(request, id):
    vehicle = get_object_or_404(Vehicle, id=id)

    brands = Brand.objects.all()

    b = brands.get(id=vehicle.brand.id)

    return render(request, 'search_inventory_result_detail.html', {'vehicle': vehicle, 'brand': b.brand_name})


# Define function to search Vehicle
@login_required(login_url='/admin/login/')
def search(request):
    results = []

    if request.method == "GET":

        query = request.GET.get('search')

        if query == '':
            query = 'None'

        results = Vehicle.objects.filter(Q(model_name__icontains=query) | Q(model_year__icontains=query) | Q(price__lte=query))

    return render(request, 'search_inventory_result.html', {'query': query, 'results': results})


def inventory_add(request):
    if request.method == 'POST':
        form = VehicleForm(request.POST)

        if form.is_valid():
            form.save()

            return redirect('inventory')
    else:
        context = {'form': VehicleForm()}
        return render(request, 'inventory_add.html', context)


@login_required(login_url='/admin/login/')
def brand_view(request):
    if request.method == 'POST':
        form = BrandForm(request.POST)

        if form.is_valid():
            form.save()

            return redirect('inventory')
    else:
        context = {'form': BrandForm()}
        return render(request, "brand.html", context)


def inventory_delete(request, id):
    Vehicle.objects.filter(id=id).delete()
    return redirect('inventory')


def inventory_edit(request, id):
    # model_id = request.GET.get('id')
    vehicle = Vehicle.objects.get(pk=id)
    form = VehicleForm(instance=vehicle)
    context = {'form': form, 'model_id':id}
    return render(request, 'inventory_edit.html', context)


def inventory_update(request, id):
    vehicle = Vehicle.objects.get(pk=id)
    form = VehicleForm(request.POST or None, instance=vehicle)

    if form.is_valid():
        form.save()
    return redirect('inventory')
