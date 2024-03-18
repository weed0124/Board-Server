from locust import HttpUser, task, between
import random


class AddPosts(HttpUser):
    wait_time = between(1, 2)

    def on_start(self):
        self.client.post("/api/member/sign-in", json={
                "loginId":"test1234",
                "password":"1234asdf"
        })

    @task
    def add_post(self):
        self.client.post("/api/board/add", json={
                "title": "테스트 게시글" + str(random.randint(1, 100000)),
                "content": "TEST".join(str(random.randint(1, 10000))),
                "nickname": "익명".join(str(random.randint(1, 10000))),
                "ip":"192.168.219.125",
                "password":"test"
                })