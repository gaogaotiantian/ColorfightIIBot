import asyncio
import websockets
import threading
import queue
import json
import time

async def game_info(ws_url, q, loop = None):
    print("Info Channel Started")
    try:
        async with websockets.connect(ws_url, max_size = None, ping_interval = None, loop = loop) as ws:
            try:
                async for info in ws:
                    q.put(json.loads(info))
                    await asyncio.sleep(0.02, loop = loop)
            except asyncio.CancelledError:
                raise
            except Exception as e:
                print(e)
    except asyncio.CancelledError:
        print("Info Channel Finished")

async def action(ws_url, q, resp_queue, loop = None):
    print("Action Channel Started")
    try:
        async with websockets.connect(ws_url, loop = loop) as ws:
            while True:
                try:
                    action = q.get(block = False)
                    await ws.send(json.dumps(action))
                    result = await ws.recv()
                    resp_queue.put(json.loads(result))
                except queue.Empty:
                    pass
                except asyncio.CancelledError:
                    raise
                except Exception as e:
                    print(e)
                    break

                await asyncio.sleep(0.02, loop = loop)
    except asyncio.CancelledError:
        print("Action Channel Finished")

class Network(threading.Thread):
    host_url = "ws://localhost:5000/"
    def __init__(self, info_queue, action_queue, action_resp_queue, url = None):
        def parse_url(url):
            '''
                change the possible url to correct form
            '''
            if url.startswith('http'):
                return url.replace('http', 'ws')
            elif not url.startswith('ws'):
                return 'ws://' + url
            return url
        threading.Thread.__init__(self)
        if url:
            self.url = parse_url(url)
        else:
            self.url = host_url
        self.info_queue = info_queue
        self.action_queue = action_queue
        self.action_resp_queue = action_resp_queue
        
    def run(self):
        print("Network started!")
        loop = asyncio.new_event_loop()
        self.loop = loop
        self.task_game_info = asyncio.Task(game_info(self.url + '/game_channel', self.info_queue, self.loop), loop = self.loop)
        self.task_action = asyncio.Task(action(self.url + '/action_channel', self.action_queue, self.action_resp_queue, self.loop), loop = self.loop)
        self.task = asyncio.gather(self.task_game_info, self.task_action, loop = self.loop)
        self.loop.run_until_complete(self.task)
        print("Network closed!")

    def disconnect(self):
        print("Disconnecting...")
        self.task_game_info.cancel()
        self.task_action.cancel()
        while self.loop.is_running():
            time.sleep(0.1)
        print("Disconnected")

if __name__ == '__main__':
    q1 = queue.Queue()
    q2 = queue.Queue()
    nw = Network(q1, q2, 'ws://localhost:5000')
    nw.start()
