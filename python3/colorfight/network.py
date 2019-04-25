import asyncio
import websockets
import threading
import queue
import json

async def game_info(ws_url, queue, loop = None):
    print("Info Channel Started")
    async with websockets.connect(ws_url, max_size = None, ping_interval = None, loop = loop) as ws:
        try:
            async for info in ws:
                queue.put(json.loads(info))
                await asyncio.sleep(0.02)
        except Exception as e:
            print(e)
            #break

async def action(ws_url, queue, resp_queue):
    print("Action Channel Started")
    async with websockets.connect(ws_url) as ws:
        while True:
            try:
                action = queue.get(block = False)
                await ws.send(json.dumps(action))
                result = await ws.recv()
                resp_queue.put(json.loads(result))
            except Exception as e:
                pass

            await asyncio.sleep(0.02)

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
        asyncio.set_event_loop(asyncio.new_event_loop())
        loop = asyncio.get_event_loop()
        self.loop = loop
        self.info_queue = info_queue
        self.action_queue = action_queue
        self.action_resp_queue = action_resp_queue
        
    def run(self):
        print("Network started!")
        asyncio.gather(game_info(self.url + '/game_channel', self.info_queue, self.loop), action(self.url + '/action_channel', self.action_queue, self.action_resp_queue), loop = self.loop)
        self.loop.run_forever()

if __name__ == '__main__':
    q1 = queue.Queue()
    q2 = queue.Queue()
    nw = Network(q1, q2, 'ws://localhost:5000')
    nw.start()
