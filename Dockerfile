FROM python:3.11

RUN ls

COPY code/django/ .

RUN pip install poetry
RUN poetry install --no-root
RUN poetry run python manage.py collectstatic --no-input
RUN poetry run python manage.py migrate

EXPOSE 8000

# poetry run python manage.py migrate
# poetry run python manage.py runserver 0.0.0.0:8000 --insecure