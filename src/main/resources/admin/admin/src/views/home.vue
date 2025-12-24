<template>
<div class="content">
  <!-- Statistics Cards -->
  <el-row :gutter="20" style="width: 100%; margin-bottom: 20px;">
    <el-col :span="6">
      <el-card shadow="hover" :body-style="{ padding: '0px' }">
        <div class="card-content">
          <div class="icon-box" style="background-color: #409EFF">
            <i class="el-icon-user-solid"></i>
          </div>
          <div class="text-box">
            <div class="count">{{ xueshengCount }}</div>
            <div class="label">学生总数</div>
          </div>
        </div>
      </el-card>
    </el-col>
    <el-col :span="6">
      <el-card shadow="hover" :body-style="{ padding: '0px' }">
        <div class="card-content">
          <div class="icon-box" style="background-color: #67C23A">
            <i class="el-icon-s-custom"></i>
          </div>
          <div class="text-box">
            <div class="count">{{ jiaoshiCount }}</div>
            <div class="label">教师总数</div>
          </div>
        </div>
      </el-card>
    </el-col>
    <el-col :span="6">
      <el-card shadow="hover" :body-style="{ padding: '0px' }">
        <div class="card-content">
          <div class="icon-box" style="background-color: #E6A23C">
             <i class="el-icon-trophy"></i>
          </div>
          <div class="text-box">
            <div class="count">{{ saixiangCount }}</div>
            <div class="label">赛项总数</div>
          </div>
        </div>
      </el-card>
    </el-col>
    <el-col :span="6">
      <el-card shadow="hover" :body-style="{ padding: '0px' }">
        <div class="card-content">
          <div class="icon-box" style="background-color: #F56C6C">
            <i class="el-icon-edit-outline"></i>
          </div>
          <div class="text-box">
            <div class="count">{{ baomingCount }}</div>
            <div class="label">报名总数</div>
          </div>
        </div>
      </el-card>
    </el-col>
  </el-row>

  <!-- Charts -->
  <el-row :gutter="20" style="width: 100%;">
    <el-col :span="12">
      <el-card shadow="hover">
        <div slot="header" class="clearfix">
          <span>赛项类型分布</span>
        </div>
        <div id="typeChart" style="width: 100%; height: 400px;"></div>
      </el-card>
    </el-col>
    <el-col :span="12">
      <el-card shadow="hover">
        <div slot="header" class="clearfix">
          <span>赛项级别统计</span>
        </div>
        <div id="levelChart" style="width: 100%; height: 400px;"></div>
      </el-card>
    </el-col>
  </el-row>
</div>
</template>
<script>
import router from '@/router/router-static'
import echarts from 'echarts'

export default {
  data() {
    return {
      xueshengCount: 0,
      jiaoshiCount: 0,
      saixiangCount: 0,
      baomingCount: 0,
      typeChart: null,
      levelChart: null
    };
  },
  mounted(){
    this.init();
    this.getAllData();
  },
  methods:{
    init(){
        if(!this.$storage.get('Token')){
            router.push({ name: 'login' })
        }
    },
    getAllData(){
        // 1. Get Counts
        this.getModuleCount('xuesheng', (count) => { this.xueshengCount = count });
        this.getModuleCount('jiaoshi', (count) => { this.jiaoshiCount = count });
        this.getModuleCount('saixiangbaoming', (count) => { this.baomingCount = count });
        
        // 2. Get Competition Info (Count + Charts)
        this.$http({
            url: `saixiangxinxi/page`,
            method: "get",
            params: {
                page: 1,
                limit: 100
            }
        }).then(({ data }) => {
            if (data && data.code === 0) {
                this.saixiangCount = data.data.total;
                this.initCharts(data.data.list);
            }
        });
    },
    getModuleCount(moduleName, callback) {
        this.$http({
            url: `${moduleName}/page`,
            method: "get",
            params: {
                page: 1,
                limit: 1
            }
        }).then(({ data }) => {
            if (data && data.code === 0) {
                callback(data.data.total);
            }
        });
    },
    initCharts(list) {
        // Process Data
        let typeMap = {};
        let levelMap = {};
        
        list.forEach(item => {
            // Count Types
            let type = item.leixing || '未知';
            typeMap[type] = (typeMap[type] || 0) + 1;
            
            // Count Levels
            let level = item.jibie || '未知';
            levelMap[level] = (levelMap[level] || 0) + 1;
        });

        // Prepare Type Chart Data
        let typeData = Object.keys(typeMap).map(key => {
            return { value: typeMap[key], name: key }
        });

        // Prepare Level Chart Data
        let levelCategories = Object.keys(levelMap);
        let levelData = Object.values(levelMap);

        // Render Type Chart
        this.typeChart = echarts.init(document.getElementById('typeChart'));
        this.typeChart.setOption({
            tooltip: {
                trigger: 'item',
                formatter: '{a} <br/>{b} : {c} ({d}%)'
            },
            legend: {
                orient: 'vertical',
                left: 'left',
                data: Object.keys(typeMap)
            },
            series: [
                {
                    name: '赛项类型',
                    type: 'pie',
                    radius: '55%',
                    center: ['50%', '60%'],
                    data: typeData,
                    emphasis: {
                        itemStyle: {
                            shadowBlur: 10,
                            shadowOffsetX: 0,
                            shadowColor: 'rgba(0, 0, 0, 0.5)'
                        }
                    },
                    itemStyle: {
                        color: function(params) {
                            var colorList = ['#409EFF', '#67C23A', '#E6A23C', '#F56C6C'];
                            return colorList[params.dataIndex % colorList.length]
                        }
                    }
                }
            ]
        });

        // Render Level Chart
        this.levelChart = echarts.init(document.getElementById('levelChart'));
        this.levelChart.setOption({
            color: ['#1E88E5'],
            tooltip: {
                trigger: 'axis',
                axisPointer: { type: 'shadow' }
            },
            grid: {
                left: '3%',
                right: '4%',
                bottom: '3%',
                containLabel: true
            },
            xAxis: [
                {
                    type: 'category',
                    data: levelCategories,
                    axisTick: { alignWithLabel: true }
                }
            ],
            yAxis: [
                {
                    type: 'value'
                }
            ],
            series: [
                {
                    name: '赛项数量',
                    type: 'bar',
                    barWidth: '60%',
                    data: levelData
                }
            ]
        });
    }
  }
};
</script>

<style lang="scss" scoped>
.content {
  padding: 20px;
  display: flex;
  flex-direction: column;
  width: 100%;
}

.card-content {
    display: flex;
    align-items: center;
    height: 100px;
}

.icon-box {
    width: 100px;
    height: 100%;
    display: flex;
    align-items: center;
    justify-content: center;
    
    i {
        font-size: 48px;
        color: #fff;
    }
}

.text-box {
    flex: 1;
    text-align: center;
    
    .count {
        font-size: 30px;
        font-weight: bold;
        color: #303133;
    }
    
    .label {
        font-size: 14px;
        color: #909399;
        margin-top: 10px;
    }
}
</style>