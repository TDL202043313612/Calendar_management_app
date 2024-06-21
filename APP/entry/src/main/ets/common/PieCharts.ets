export declare interface PieChartsData {
  getValue(): number
}

export class PieChartsLabelStyle {
  fontSize: number = 14
  fontColor: string | Color = Color.Orange
}

@Observed
export class PieChartsOptions {

  radius: number = 60
  innerRadius: number = 0
  data: PieChartsData[] = []
  animate: boolean = true
  duration: number = 500
  labelFn: (data: PieChartsData, index: number) => string = () => {
    return 'unknown'
  }
  labelStyleFn: (data: PieChartsData, index: number) => PieChartsLabelStyle = () => {
    return new PieChartsLabelStyle()
  }
  colorFn: (data: PieChartsData, index: number) => string | Color = (data: PieChartsData, index: number) => {
    let colors = [Color.Red, Color.Green, Color.Blue, Color.Orange, Color.Grey, Color.Pink, '#FFCC00', '#800080', '#ff6633', '#ffcc66']
    return colors[index % colors.length]
  }
  leaderLineColorFn: (data: PieChartsData, index: number) => string | Color = (data: PieChartsData, index: number) => {
    return Color.Orange
  }
}

@Component
export struct PieCharts {

  @Watch('animateDraw') @Prop options: PieChartsOptions
  private settings: RenderingContextSettings = new RenderingContextSettings(true)
  private context: CanvasRenderingContext2D = new CanvasRenderingContext2D(this.settings)

  animateDraw() {
    console.log(`animateDraw`)
    if (!this.options.animate) {
      this.draw(1)
      return
    }

    let count = 0
    let interval = setInterval(() => {
      count++
      if (count == 60) {
        clearInterval(interval)
      }
      this.draw(count / 60)
    }, this.options.duration / 60)
  }

  draw(progress: number) {
    this.clearCanvas()

    const data = this.options.data
    const radius = this.options.radius
    const innerRadius = this.options.innerRadius
    const brokenLineLength = 15
    const brokenLineWidth = 1.5
    const arcWidth = radius - innerRadius
    const arcRadius = innerRadius + arcWidth / 2

    let centerX = this.context.width / 2
    let centerY = this.context.height / 2

    const totalValue = data.reduce((acc, item) => acc + item.getValue(), 0)
    const percentages: number[] = data.map(item => item.getValue() / totalValue);

    let startAngle = -Math.PI / 2
    for (let i = 0; i < percentages.length; i++) {
      let percent = percentages[i]
      let angle = percent * 2 * Math.PI * progress
      let endAngle = startAngle + angle

      let item = data[i]
      let color = this.options.colorFn(item, i)

      // 画扇形
      this.context.beginPath()
      this.context.arc(centerX, centerY, arcRadius, startAngle, endAngle)
      this.context.lineWidth = arcWidth
      // @ts-ignore
      this.context.strokeStyle = color
      this.context.stroke()
      this.context.restore()

      // 角度小于Math.PI / 12，不显示label
      if (angle <= Math.PI / 12) {
        startAngle = endAngle
        continue
      }

      // 画折线
      let centerAngle = startAngle + angle / 2
      let r = radius + brokenLineLength / 2

      let x1 = centerX + (r - brokenLineLength) * Math.cos(centerAngle)
      let y1 = centerY + (r - brokenLineLength) * Math.sin(centerAngle)

      let x2 = centerX + r * Math.cos(centerAngle)
      let y2 = centerY + r * Math.sin(centerAngle)

      let x3 = x2
      let y3 = y2
      if (centerAngle < Math.PI / 2) {
        this.context.textAlign = 'right'
        x3 = x2 + 15
      } else {
        this.context.textAlign = 'left'
        x3 = x2 - 15
      }

      // 折线
      let leaderLineColor = this.options.leaderLineColorFn(item, i)
      this.context.beginPath()
      this.context.lineWidth = brokenLineWidth
      // @ts-ignore
      this.context.strokeStyle = leaderLineColor
      this.context.moveTo(x1, y1)
      this.context.lineTo(x2, y2)
      this.context.lineTo(x3, y3)
      this.context.stroke()

      // 画文字
      // 设置字体样式
      const labelStyle = this.options.labelStyleFn(item, i)
      this.context.textBaseline = 'middle'
      // @ts-ignore
      this.context.fillStyle = labelStyle.fontColor
      this.context.font = fp2px(labelStyle.fontSize) + 'px sans-serif'
      // 获取文本
      let label = this.options.labelFn(data[i], i)
      let textWidth = this.context.measureText(label).width

      let x4 = x3
      let y4 = y3
      if (centerAngle < Math.PI / 2) {
        this.context.textAlign = 'right'
        x3 = x2 + 15
        x4 = x3 + textWidth + 3
      } else {
        this.context.textAlign = 'left'
        x3 = x2 - 15
        x4 = x3 - textWidth - 3
      }

      this.context.fillText(label, x4, y4)
      this.context.stroke()

      startAngle = endAngle
    }
  }

  clearCanvas() {
    this.context.restore();
    this.context.resetTransform();
    this.context.clearRect(0, 0, this.context.width, this.context.height);
  }

  build() {
    Column() {
      Canvas(this.context)
        .width('100%')
        .height('100%')
        .backgroundColor(Color.White)
        .onReady(() => {
          this.animateDraw()
        })
    }
    .size({ width: '100%', height: "50%" })
  }
}