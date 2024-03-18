import json

from locust import HttpUser, task, between
import random


class BoardServer(HttpUser):
    wait_time = between(1, 2)

    def on_start(self):
        self.client.post("/api/member/sign-in", json={
                "loginId":"test1234",
                "password":"1234asdf"
        })

    @task(1)
    def view_item(self):
        name = '테스트 게시글'.join(str(random.randint(1, 10000)))
        headers = {'Content-Type': 'application/json'}
        data = {
                "title": name
                }
        # print(data)
        self.client.get("/api/board", json=data, headers=headers)