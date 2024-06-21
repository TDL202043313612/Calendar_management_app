<template>
  <div class="signatureBox">
    <div ref="canvasHW" class="canvasBox">
      <canvas ref="canvasF"
              @touchstart="touchStart"
              @touchmove="touchMove"
              @touchend="touchEnd"
              @mousedown="mouseDown"
              @mousemove="mouseMove"
              @mouseup="mouseUp">
      </canvas>
    </div>
    <div class="signature-btnArea">
      <!--<el-button class="btn" type="info" @click.native.prevent="handleGoBack()">返回</el-button>-->
      <el-button class="btn" type="info" @click.native.prevent="handleOverwrite()">重签</el-button>
      <el-button class="btn" type="primary" @click.native.prevent="handleSubmit()">确认</el-button>
    </div>
  </div>
</template>

<script>
  export default {
    name: 'ESignatureFangdong',
    data() {
      return {
        points: [],
        canvasTxt: null,
        stage_info: [],
        startX: 0,
        startY: 0,
        moveY: 0,
        moveX: 0,
        isDown: false,
        strokeStyle: '#000',
        lineWidth: 2
      }
    },
    mounted() {
      this.initCanvas()
    },
    methods: {
      // 初始化Canvas
      initCanvas() {
        let canvas = this.$refs.canvasF
        // 获取画布的高度
        canvas.height = this.$refs.canvasHW.offsetHeight -20
        // 获取画布的宽度
        canvas.width = this.$refs.canvasHW.offsetWidth - ((this.$refs.canvasHW.offsetWidth)/2)
        // 创建 context 对象
        this.canvasTxt = canvas.getContext('2d')
        this.stage_info = canvas.getBoundingClientRect()
      },
      // 鼠标按下事件 - 准备绘画
      mouseDown(ev) {
        ev = ev || event
        ev.preventDefault()
        if (ev) {
          let obj = {
            x: ev.offsetX,
            y: ev.offsetY
          }
          this.startX = obj.x
          this.startY = obj.y
          this.canvasTxt.beginPath()
          this.canvasTxt.moveTo(this.startX, this.startY)
          this.canvasTxt.lineTo(obj.x, obj.y)
          this.canvasTxt.stroke()
          this.canvasTxt.closePath()
          this.points.push(obj)
          this.isDown = true
        }
      },
      // 触摸开始事件 - 准备绘画
      touchStart(ev) {
        ev = ev || event
        ev.preventDefault()
        if (ev.touches.length == 1) {
          let obj = {
            x: ev.targetTouches[0].clientX - this.stage_info.left,
            y: ev.targetTouches[0].clientY - this.stage_info.top
          }
          this.startX = obj.x
          this.startY = obj.y
          this.canvasTxt.beginPath()
          this.canvasTxt.moveTo(this.startX, this.startY)
          this.canvasTxt.lineTo(obj.x, obj.y)
          this.canvasTxt.stroke()
          this.canvasTxt.closePath()
          this.points.push(obj)
        }
      },
      // 鼠标移动事件 - 开始绘画
      mouseMove(ev) {
        ev = ev || event
        ev.preventDefault()
        if (this.isDown) {
          let obj = {
            x: ev.offsetX,
            y: ev.offsetY
          }
          this.moveY = obj.y
          this.moveX = obj.x
          this.canvasTxt.strokeStyle = this.strokeStyle
          this.canvasTxt.lineWidth = this.lineWidth
          this.canvasTxt.beginPath()
          this.canvasTxt.moveTo(this.startX, this.startY)
          this.canvasTxt.lineTo(obj.x, obj.y)
          this.canvasTxt.stroke()
          this.canvasTxt.closePath()
          this.startY = obj.y
          this.startX = obj.x
          this.points.push(obj)
        }
      },
      // 触摸移动事件 - 开始绘画
      touchMove(ev) {
        ev = ev || event
        ev.preventDefault()
        if (ev.touches.length == 1) {
          let obj = {
            x: ev.targetTouches[0].clientX - this.stage_info.left,
            y: ev.targetTouches[0].clientY - this.stage_info.top
          }
          this.moveY = obj.y
          this.moveX = obj.x
          // 设置线条颜色
          this.canvasTxt.strokeStyle = this.strokeStyle
          // 设置线条宽度
          this.canvasTxt.lineWidth = this.lineWidth
          // 绘制开始路径
          this.canvasTxt.beginPath()
          // 定义线条开始坐标
          this.canvasTxt.moveTo(this.startX, this.startY)
          // 定义线条结束坐标
          this.canvasTxt.lineTo(obj.x, obj.y)
          // 绘制线条
          this.canvasTxt.stroke()
          this.canvasTxt.closePath()
          this.startY = obj.y
          this.startX = obj.x
          this.points.push(obj)
        }
      },
      // 松开鼠标事件 - 停止绘画
      mouseUp(ev) {
        ev = ev || event
        ev.preventDefault()
        if (ev) {
          let obj = {
            x: ev.offsetX,
            y: ev.offsetY
          }
          this.canvasTxt.beginPath()
          this.canvasTxt.moveTo(this.startX, this.startY)
          this.canvasTxt.lineTo(obj.x, obj.y)
          this.canvasTxt.stroke()
          this.canvasTxt.closePath()
          this.points.push(obj)
          this.points.push({ x: -1, y: -1 })
          this.isDown = false
        }
      },
      // 触摸结束事件 - 停止绘画
      touchEnd(ev) {
        ev = ev || event
        ev.preventDefault()
        if (ev.touches.length == 1) {
          let obj = {
            x: ev.targetTouches[0].clientX - this.stage_info.left,
            y: ev.targetTouches[0].clientY - this.stage_info.top
          }
          this.canvasTxt.beginPath()
          this.canvasTxt.moveTo(this.startX, this.startY)
          this.canvasTxt.lineTo(obj.x, obj.y)
          this.canvasTxt.stroke()
          this.canvasTxt.closePath()
          this.points.push(obj)
          this.points.push({ x: -1, y: -1 })
        }
      },
      // 返回
      handleGoBack() {
        this.handleOverwrite()
        this.$emit('on-back')
      },
      // 重写
      handleOverwrite() {
        this.canvasTxt.clearRect(0, 0, this.$refs.canvasF.width, this.$refs.canvasF.height)
        this.points = []
      },
      // 提交
      handleSubmit() {
        if (this.points.length < 50) {
          if (this.points.length == 0) {
            this.$message.error('请签名！');
          }
          return
        }
        this.$emit('fangdongSign', this.$refs.canvasF.toDataURL())
        this.handleOverwrite()
      }
    }
  }
</script>
