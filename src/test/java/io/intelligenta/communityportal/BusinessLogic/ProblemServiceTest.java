package io.intelligenta.communityportal.BusinessLogic;

import io.intelligenta.communityportal.models.NAP;
import io.intelligenta.communityportal.models.NAPArea;
import io.intelligenta.communityportal.models.Problem;
import io.intelligenta.communityportal.models.StrategyGoal;
import io.intelligenta.communityportal.models.dto.ProblemDto;
import io.intelligenta.communityportal.models.exceptions.InstitutionNotFoundException;
import io.intelligenta.communityportal.models.exceptions.NAPAreaNotFoundException;
import io.intelligenta.communityportal.models.exceptions.NAPNotFoundException;
import io.intelligenta.communityportal.models.exceptions.ProblemNotFoundException;
import io.intelligenta.communityportal.repository.NAPAreaRepository;
import io.intelligenta.communityportal.repository.NAPRepository;
import io.intelligenta.communityportal.repository.ProblemRepository;
import io.intelligenta.communityportal.repository.StrategyGoalRepository;
import io.intelligenta.communityportal.repository.auth.UserRepository;
import io.intelligenta.communityportal.service.ProblemService;
import io.intelligenta.communityportal.service.impl.ProblemServiceImpl;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RunWith(MockitoJUnitRunner.class)
public class ProblemServiceTest {
    @Mock
    private  ProblemRepository problemRepository;
    @Mock
    private  UserRepository userRepository;
    @Mock
    private  NAPAreaRepository napAreaRepository;
    @Mock
    private  NAPRepository napRepository;
    @Mock
    private  StrategyGoalRepository strategyGoalRepository;

    ProblemService problemService;

    @Before
    public void init(){
        MockitoAnnotations.initMocks(this);
        Problem problem = new Problem();
        Problem problem1 = new Problem();
        problem.setId(1L);
        problem.setNameMk("Проблем мк");
        problem.setActive(true);
        problem1.setId(2L);
        problem1.setActive(false);
        StrategyGoal strategyGoal = new StrategyGoal();
        StrategyGoal strategyGoal1 = new StrategyGoal();
        strategyGoal.setId(1L);
        strategyGoal1.setId(2L);
        NAP nap = new NAP();
        nap.setId(1L);
        NAPArea napArea = new NAPArea();
        napArea.setId(1L);
        problem.setNapArea(napArea);
        List<Problem> problemsWithNapArea = new ArrayList<>();
        problemsWithNapArea.add(problem);
        Mockito.when(this.problemRepository.findAllByNapArea(napArea)).thenReturn(problemsWithNapArea);
        Mockito.when(this.problemRepository.save(problem)).thenReturn(problem);
        Mockito.when(this.problemRepository.save(problem1)).thenReturn(problem1);
        Mockito.when(this.problemRepository.findById(1L)).thenReturn(Optional.of(problem));
        Mockito.when(this.problemRepository.findById(2L)).thenReturn(Optional.of(problem1));
        Mockito.when(this.napRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(nap));
        Mockito.when(this.napAreaRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(napArea));
        Mockito.when(this.strategyGoalRepository.findById(1L)).thenReturn(Optional.of(strategyGoal));
        Mockito.when(this.strategyGoalRepository.findById(2L)).thenReturn(Optional.of(strategyGoal1));

        this.problemService = Mockito.spy(new ProblemServiceImpl(
                this.problemRepository,
                this.userRepository,
                this.napAreaRepository,
                this.napRepository,
                this.strategyGoalRepository
        ));
    }

    @Test
    public void testCreateProblem(){
        ProblemDto problemDto = new ProblemDto();
        StrategyGoal strategyGoal = new StrategyGoal();
        StrategyGoal strategyGoal1 = new StrategyGoal();
        strategyGoal.setId(1L);
        strategyGoal1.setId(2L);
        List<Long> strategyGoalList = new ArrayList<>();
        strategyGoalList.add(strategyGoal.getId());
        strategyGoalList.add(strategyGoal1.getId());
        problemDto.setStrategyGoals(strategyGoalList);
        problemDto.setNameMk("Проблем мк");
        problemDto.setNameAl("");
        problemDto.setNameEn("");
        problemDto.setDescriptionAl("");
        problemDto.setDescriptionMk("");
        problemDto.setDescriptionEn("");
        Assertions.assertThrows(NAPNotFoundException.class, ()-> this.problemService.createProblem(problemDto));
        problemDto.setNap(1L);
        Mockito.verify(this.problemService).createProblem(problemDto);
        Assertions.assertThrows(NAPAreaNotFoundException.class, ()-> this.problemService.createProblem(problemDto));
        problemDto.setNapArea(1L);
        Problem p = this.problemService.findById(1L);
        Mockito.when(this.problemRepository.save(Mockito.any(Problem.class))).thenReturn(p);
        Problem problem = this.problemService.createProblem(problemDto);
        Assert.assertNotNull(problem);
    }

    @Test(expected = InstitutionNotFoundException.class)
    public void testCreateProblemWithNonexistentInstitution(){
        ProblemDto problemDto = new ProblemDto();
        StrategyGoal strategyGoal = new StrategyGoal();
        StrategyGoal strategyGoal1 = new StrategyGoal();
        strategyGoal.setId(1L);
        strategyGoal1.setId(200L);
        List<Long> strategyGoalList = new ArrayList<>();
        strategyGoalList.add(strategyGoal.getId());
        strategyGoalList.add(strategyGoal1.getId());
        problemDto.setStrategyGoals(strategyGoalList);
        problemDto.setId(1L);
        Problem problem = this.problemService.createProblem(problemDto);
        Mockito.verify(this.problemService).createProblem(problemDto);
    }

    @Test
    public void testFindProblemById(){
        Problem problem = this.problemService.findById(1L);
        Mockito.verify(this.problemService).findById(1L);
        Assert.assertNotNull(problem);
        Assert.assertTrue(problem.getId() == 1L);
        Assert.assertEquals(problem.getNameMk(), "Проблем мк");
    }

    @Test(expected = ProblemNotFoundException.class)
    public void testFindProblemByNonexistentId(){
        Problem problem = this.problemService.findById(100L);
        Mockito.verify(this.problemService).findById(100L);
        Assert.assertNotNull(problem);
    }

    @Test
    public void testUpdateProblem(){
        ProblemDto problemDto = new ProblemDto();
        problemDto.setId(1L);
        StrategyGoal strategyGoal = new StrategyGoal();
        StrategyGoal strategyGoal1 = new StrategyGoal();
        strategyGoal.setId(1L);
        strategyGoal1.setId(2L);
        List<Long> strategyGoalList = new ArrayList<>();
        strategyGoalList.add(strategyGoal.getId());
        strategyGoalList.add(strategyGoal1.getId());
        problemDto.setStrategyGoals(strategyGoalList);
        problemDto.setNameMk("Проблем мк");
        problemDto.setNameAl("Problem al");
        problemDto.setNameEn("Problem en");
        problemDto.setDescriptionAl("");
        problemDto.setDescriptionMk("");
        problemDto.setDescriptionEn("");
        Problem problem = this.problemService.updateProblem(problemDto);
        Mockito.verify(this.problemService).updateProblem(problemDto);
        Assert.assertNotNull(problem);
        Assert.assertTrue(problem.getId() == problemDto.getId());
        Assert.assertEquals(problem.getNameMk(), problemDto.getNameMk());
        Assert.assertEquals(problem.getNameAl(), problemDto.getNameAl());
        Assert.assertEquals(problem.getNameEn(), problemDto.getNameEn());
    }

    @Test
    public void testSetProblemInactive(){
        Problem problem = this.problemService.setInactive(1L);
        Mockito.verify(this.problemService).setInactive(problem.getId());
        Assert.assertNotNull(problem);
        Assert.assertFalse(problem.getActive());
        Assertions.assertThrows(ProblemNotFoundException.class, ()-> this.problemService.setInactive(100L));

    }

    @Test
    public void testSetProblemActive(){
        Problem problem = this.problemService.setActive(2L);
        Mockito.verify(this.problemService).setActive(problem.getId());
        Assert.assertNotNull(problem);
        Assert.assertTrue(problem.getActive());
        Assertions.assertThrows(ProblemNotFoundException.class, ()-> this.problemService.setActive(100L));
    }

    @Test
    public void tesFindAllProblemsByNapAreaId(){
        List<Problem> problemsWithNapArea = this.problemService.findAllByNapAreaId(1L);
        Mockito.verify(this.problemService).findAllByNapAreaId(1L);
        Assert.assertNotNull(problemsWithNapArea);
        Assert.assertEquals(problemsWithNapArea.size(), 1);
        Assert.assertEquals(problemsWithNapArea.get(0), problemService.findById(1L));
    }

}
